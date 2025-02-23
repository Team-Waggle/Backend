package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.*;
import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.exception.AccessDeniedException;
import com.waggle.global.response.ApiStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ReferenceService referenceService;

    @Override
    public Project getProjectByProjectId(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public Project createProject(ProjectInputDto projectInputDto) {
        Project newProject = Project.builder()
                .title(projectInputDto.getTitle())
                .industry(referenceService.getIndustryById(projectInputDto.getIndustryId()))
                .waysOfWorking(referenceService.getWaysOfWorkingById(projectInputDto.getWayOfWorkingId()))
                .recruitmentDate(projectInputDto.getRecruitmentDate())
                .durationOfWorking(referenceService.getDurationOfWorkingById(projectInputDto.getDurationOfWorkingId()))
                .detail(projectInputDto.getDetail())
                .connectUrl(projectInputDto.getConnectUrl())
                .referenceUrl(projectInputDto.getReferenceUrl())
                .bookmarkCnt(0)
                .build();
        newProject = projectRepository.save(newProject);

        Set<ProjectMember> projectMembers = new HashSet<>();
        projectMembers.add(ProjectMember.builder()
                .project(newProject)
                .user(userService.getCurrentUser())
                .isLeader(true)
                .build());
        newProject.setProjectMembers(projectMembers);
        newProject.setRecruitmentJobs(getProjectRecruitmentJobs(projectInputDto, newProject));
        newProject.setMemberJobs(getProjectMemberJobs(projectInputDto, newProject));
        newProject.setProjectSkills(getProjectSkills(projectInputDto, newProject));
        projectRepository.save(newProject);

        return newProject;
    }

    @Override
    public Project updateProject(UUID id, ProjectInputDto projectInputDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._UPDATE_ACCESS_DENIED);
        }

        project.setTitle(projectInputDto.getTitle());
        project.setIndustry(referenceService.getIndustryById(projectInputDto.getIndustryId()));
        project.setWaysOfWorking(referenceService.getWaysOfWorkingById(projectInputDto.getWayOfWorkingId()));
        project.setRecruitmentDate(projectInputDto.getRecruitmentDate());
        project.setDurationOfWorking(referenceService.getDurationOfWorkingById(projectInputDto.getDurationOfWorkingId()));
        project.setRecruitmentJobs(getProjectRecruitmentJobs(projectInputDto, project));
        project.setMemberJobs(getProjectMemberJobs(projectInputDto, project));
        project.setProjectSkills(getProjectSkills(projectInputDto, project));
        project.setDetail(projectInputDto.getDetail());
        project.setConnectUrl(projectInputDto.getConnectUrl());
        project.setReferenceUrl(projectInputDto.getReferenceUrl());
        projectRepository.save(project);
        return project;
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
        //id가 아닌 다른 기준으로 값을 삭제할 때
        /*Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        projectRepository.delete(project);*/

        //id 기준
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._DELETE_ACCESS_DENIED);
        }

        projectRepository.delete(project);
    }

    @Override
    public Set<User> getUsersByProjectId(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        Map<User, Map.Entry<Long, LocalDateTime>> userJobMap = project.getProjectMembers().stream()
                .collect(Collectors.toMap(
                        ProjectMember::getUser,
                        projectMember -> Map.entry(
                                projectMember.getUser().getUserJobs().stream()
                                        .map(userJob -> userJob.getJob().getId())
                                        .findFirst()
                                        .orElse(null),
                                projectMember.getJoinedAt()
                        )
                ));

        // job id 기준으로 정렬하고, job id가 같은 경우 joined_at 기준으로 정렬
        return userJobMap.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<User, Map.Entry<Long, LocalDateTime>> entry) -> entry.getValue().getKey())
                        .thenComparing(entry -> entry.getValue().getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<User> approveAppliedUser(UUID projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._UPDATE_ACCESS_DENIED);
        }

        User user = userService.getUserByUserId(userId);
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .isLeader(false)
                .joinedAt(LocalDateTime.now())
                .build();
        project.getProjectMembers().add(projectMember);;

        project.getProjectApplicants().removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    public Set<User> rejectAppliedUser(UUID projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._UPDATE_ACCESS_DENIED);
        }

        User user = userService.getUserByUserId(userId);
        project.getProjectApplicants().removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    public Set<User> rejectMemberUser(UUID projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._UPDATE_ACCESS_DENIED);
        }

        User user = userService.getUserByUserId(userId);
        project.getProjectMembers().removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    private Set<ProjectRecruitmentJob> getProjectRecruitmentJobs(ProjectInputDto projectInputDto, Project project) {
        Set<ProjectRecruitmentJob> projectRecruitmentJobs = new HashSet<>();
        projectInputDto.getRecruitmentJobs().forEach(jobDto -> {
            log.info("jobDto: {}", jobDto);
            ProjectRecruitmentJob projectRecruitmentJob = ProjectRecruitmentJob.builder()
                    .project(project)
                    .job(referenceService.getJobById(jobDto.getJobId()))
                    .recruitmentCnt(jobDto.getCnt())
                    .build();
            projectRecruitmentJobs.add(projectRecruitmentJob);
        });
        return projectRecruitmentJobs;
    }

    private Set<ProjectMemberJob> getProjectMemberJobs(ProjectInputDto projectInputDto, Project project) {
        Set<ProjectMemberJob> projectMemberJobs = new HashSet<>();
        projectInputDto.getMemberJobs().forEach(jobDto -> {
            ProjectMemberJob projectMemberJob = ProjectMemberJob.builder()
                    .project(project)
                    .job(referenceService.getJobById(jobDto.getJobId()))
                    .memberCnt(jobDto.getCnt())
                    .build();
            projectMemberJobs.add(projectMemberJob);
        });
        return projectMemberJobs;
    }

    private Set<ProjectSkill> getProjectSkills(ProjectInputDto projectInputDto, Project project) {
        Set<ProjectSkill> projectSkills = new HashSet<>();
        projectInputDto.getSkillIds().forEach(skillId -> {
            ProjectSkill projectSkill = ProjectSkill.builder()
                    .project(project)
                    .skill(referenceService.getSkillById(skillId))
                    .build();
            projectSkills.add(projectSkill);
        });
        return projectSkills;
    }

    private User getLeader(Project project) {
        return project.getProjectMembers().stream()
                .filter(ProjectMember::isLeader)
                .map(ProjectMember::getUser)
                .findFirst()
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}
