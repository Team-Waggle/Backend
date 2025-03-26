package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.project.entity.ProjectBookmark;
import com.waggle.domain.project.entity.ProjectMember;
import com.waggle.domain.project.entity.ProjectMemberJob;
import com.waggle.domain.project.entity.ProjectRecruitmentJob;
import com.waggle.domain.project.entity.ProjectSkill;
import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.exception.AccessDeniedException;
import com.waggle.global.exception.ProjectException;
import com.waggle.global.response.ApiStatus;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
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
            .title(projectInputDto.title())
            .industry(referenceService.getIndustryById(projectInputDto.industryId()))
            .waysOfWorking(
                referenceService.getWaysOfWorkingById(projectInputDto.wayOfWorkingId()))
            .recruitmentDate(projectInputDto.recruitmentDate())
            .durationOfWorking(
                referenceService.getDurationOfWorkingById(projectInputDto.durationOfWorkingId()))
            .detail(projectInputDto.detail())
            .connectUrl(projectInputDto.connectUrl())
            .referenceUrl(projectInputDto.referenceUrl())
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
        newProject.setProjectApplicants(new HashSet<>());
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
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        project.setTitle(projectInputDto.title());
        project.setIndustry(referenceService.getIndustryById(projectInputDto.industryId()));
        project.setWaysOfWorking(
            referenceService.getWaysOfWorkingById(projectInputDto.wayOfWorkingId()));
        project.setRecruitmentDate(projectInputDto.recruitmentDate());
        project.setDurationOfWorking(
            referenceService.getDurationOfWorkingById(projectInputDto.durationOfWorkingId()));
        project.setRecruitmentJobs(getProjectRecruitmentJobs(projectInputDto, project));
        project.setMemberJobs(getProjectMemberJobs(projectInputDto, project));
        project.setProjectSkills(getProjectSkills(projectInputDto, project));
        project.setDetail(projectInputDto.detail());
        project.setConnectUrl(projectInputDto.connectUrl());
        project.setReferenceUrl(projectInputDto.referenceUrl());
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
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        projectRepository.delete(project);
    }

    @Override
    public Set<User> getUsersByProjectId(UUID id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));

        return project.getProjectMembers().stream()
            .map(ProjectMember::getUser)
            .sorted(Comparator
                .comparing((User user) -> user.getUserJobs().stream()
                    .map(userJob -> userJob.getJob().getId())
                    .min(Long::compareTo)
                    .orElse(Long.MAX_VALUE)) // job_id 기준 정렬, 없으면 가장 큰 값으로
                .thenComparing(User::getName)) // job_id가 같으면 이름순 정렬
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<User> getAppliedUsersByProjectId(UUID id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));

        return project.getProjectApplicants().stream()
            .map(ProjectApplicant::getUser)
            .sorted(Comparator
                .comparing((User user) -> user.getUserJobs().stream()
                    .map(userJob -> userJob.getJob().getId())
                    .min(Long::compareTo)
                    .orElse(Long.MAX_VALUE)) // job_id 기준 정렬, 없으면 가장 큰 값으로
                .thenComparing(User::getName)) // job_id가 같으면 이름순 정렬
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public Set<User> approveAppliedUser(UUID projectId, String userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserByUserId(userId);
        ProjectMember projectMember = ProjectMember.builder()
            .project(project)
            .user(user)
            .isLeader(false)
            .joinedAt(LocalDateTime.now())
            .build();
        project.getProjectMembers().add(projectMember);
        ;

        project.getProjectApplicants()
            .removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    @Transactional
    public Set<User> rejectAppliedUser(UUID projectId, String userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserByUserId(userId);
        project.getProjectApplicants()
            .removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    @Transactional
    public Set<User> rejectMemberUser(UUID projectId, String userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserByUserId(userId);
        project.getProjectMembers()
            .removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    public void delegateLeader(UUID projectId, String userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserByUserId(userId);

        project.getProjectMembers().stream()
            .filter(member -> member.getUser().getId().equals(currentUser.getId()))
            .findFirst()
            .ifPresent(member -> member.setLeader(false));

        project.getProjectMembers().stream()
            .filter(member -> member.getUser().getId().equals(user.getId()))
            .findFirst()
            .ifPresent(member -> member.setLeader(true));

        projectRepository.save(project);
    }

    @Override
    public Set<Project> getUserProjects(String userId) {
        User user = userService.getUserByUserId(userId);
        return user.getProjectMembers().stream()
            .map(ProjectMember::getProject)
            .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void deleteUserProject(String projectId) {
        User user = userService.getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
            .orElseThrow(() -> new EmptyResultDataAccessException(1));

        if (!project.getProjectMembers().stream()
            .anyMatch(projectMember -> projectMember.getUser().getId().equals(user.getId()))) {
            throw new ProjectException(ApiStatus._NOT_JOINED_PROJECT);
        }

        project.getProjectMembers()
            .removeIf(projectMember -> projectMember.getUser().getId().equals(user.getId()));
        projectRepository.save(project);
    }

    @Override
    public Set<Project> getUserBookmarkProjects(String userId) {
        User user = userService.getUserByUserId(userId);
        return user.getProjectBookmarks().stream()
            .map(ProjectBookmark::getProject)
            .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Project applyProject(String projectId) {
        User user = userService.getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
            .orElseThrow(() -> new EmptyResultDataAccessException(1));

        if (project.getProjectMembers().stream()
            .anyMatch(projectMember -> projectMember.getUser().getId().equals(user.getId()))) {
            throw new ProjectException(ApiStatus._ALREADY_JOINED_PROJECT);
        }

        if (project.getProjectApplicants().stream()
            .anyMatch(projectMember -> projectMember.getUser().getId().equals(user.getId()))) {
            throw new ProjectException(ApiStatus._ALREADY_APPLIED_PROJECT);
        }

        project.getProjectApplicants().add(ProjectApplicant.builder()
            .project(project)
            .user(user)
            .build());

        projectRepository.save(project);
        return project;
    }

    @Override
    @Transactional
    public void cancelApplyProject(String projectId) {
        User user = userService.getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
            .orElseThrow(() -> new EmptyResultDataAccessException(1));

        project.getProjectApplicants()
            .removeIf(projectApplicant -> projectApplicant.getUser().getId().equals(user.getId()));
        projectRepository.save(project);
    }

    @Override
    public Set<Project> getAppliedProjects() {
        User user = userService.getCurrentUser();
        return user.getProjectApplicants().stream()
            .sorted(Comparator.comparing(ProjectApplicant::getAppliedAt).reversed())
            .map(ProjectApplicant::getProject)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Project> getCurrentUserProjects() {
        User user = userService.getCurrentUser();
        return user.getProjectMembers().stream()
            .map(ProjectMember::getProject)
            .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<ProjectRecruitmentJob> getProjectRecruitmentJobs(
        ProjectInputDto projectInputDto,
        Project project
    ) {
        Set<ProjectRecruitmentJob> projectRecruitmentJobs = new HashSet<>();
        projectInputDto.recruitmentJobs().forEach(jobDto -> {
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

    private Set<ProjectMemberJob> getProjectMemberJobs(
        ProjectInputDto projectInputDto,
        Project project
    ) {
        Set<ProjectMemberJob> projectMemberJobs = new HashSet<>();
        projectInputDto.memberJobs().forEach(jobDto -> {
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
        projectInputDto.skillIds().forEach(skillId -> {
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

    @Override
    public boolean toggleCurrentUserBookmark(String projectId) {
        User user = userService.getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        boolean isBookmarked;

        if (user.getProjectBookmarks().stream().anyMatch(
            projectBookmark -> projectBookmark.getProject().getId().equals(project.getId()))) {
            user.getProjectBookmarks().removeIf(
                projectBookmark -> projectBookmark.getProject().getId().equals(project.getId()));
            project.setBookmarkCnt(project.getBookmarkCnt() - 1);
            isBookmarked = false;
        } else {
            user.getProjectBookmarks().add(ProjectBookmark.builder()
                .project(project)
                .user(user)
                .build());
            project.setBookmarkCnt(project.getBookmarkCnt() + 1);
            isBookmarked = true;
        }

        userRepository.save(user);
        projectRepository.save(project);

        return isBookmarked;
    }

    @Override
    public Set<Project> getCurrentUserBookmarkProjects() {
        User user = userService.getCurrentUser();
        return user.getProjectBookmarks().stream()
            .map(ProjectBookmark::getProject)
            .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
