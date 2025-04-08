package com.waggle.domain.project.service;

import com.waggle.domain.auth.service.AuthService;
import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.dto.ProjectRecruitmentDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.project.entity.ProjectBookmark;
import com.waggle.domain.project.entity.ProjectMember;
import com.waggle.domain.project.entity.ProjectRecruitment;
import com.waggle.domain.project.entity.ProjectSkill;
import com.waggle.domain.project.repository.ProjectApplicantRepository;
import com.waggle.domain.project.repository.ProjectBookmarkRepository;
import com.waggle.domain.project.repository.ProjectMemberRepository;
import com.waggle.domain.project.repository.ProjectRecruitmentRepository;
import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.project.repository.ProjectSkillRepository;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.exception.AccessDeniedException;
import com.waggle.global.exception.ProjectException;
import com.waggle.global.response.ApiStatus;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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

    private final AuthService authService;
    private final UserService userService;
    private final ReferenceService referenceService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectApplicantRepository projectApplicantRepository;
    private final ProjectBookmarkRepository projectBookmarkRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRecruitmentRepository projectRecruitmentRepository;
    private final ProjectSkillRepository projectSkillRepository;

    @Override
    public Project getProjectByProjectId(UUID projectId) {
        return projectRepository.findById(projectId).orElseThrow(() ->
            new EntityNotFoundException("Project not found with id: " + projectId));
    }

    @Override
    @Transactional
    public Project createProject(ProjectInputDto projectInputDto) {
        Project project = Project.builder()
            .title(projectInputDto.title())
            .industry(projectInputDto.industry())
            .workWay(projectInputDto.workWay())
            .recruitmentEndDate(projectInputDto.recruitmentEndDate())
            .workPeriod(projectInputDto.workPeriod())
            .detail(projectInputDto.detail())
            .contactUrl(projectInputDto.contactUrl())
            .referenceUrl(projectInputDto.referenceUrl())
            .bookmarkCount(0)
            .build();
        project = projectRepository.save(project);

        User user = authService.getCurrentUser();
        ProjectMember projectMember = createProjectMember(project, user, true);
        projectMemberRepository.save(projectMember);

        Set<ProjectSkill> projectSkills = createProjectSkills(project, projectInputDto.skills());
        projectSkillRepository.saveAll(projectSkills);

        Set<ProjectRecruitment> projectRecruitments = createProjectRecruitments(
            project,
            projectInputDto.projectRecruitmentDtos()
        );
        projectRecruitmentRepository.saveAll(projectRecruitments);

        return project;
    }

    @Override
    @Transactional
    public Project updateProject(UUID projectId, ProjectInputDto projectInputDto) {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
            new EntityNotFoundException("Project not found with id: " + projectId));
        User currentUser = authService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        project.update(
            projectInputDto.title(),
            projectInputDto.industry(),
            projectInputDto.workWay(),
            projectInputDto.recruitmentEndDate(),
            projectInputDto.workPeriod(),
            projectInputDto.detail(),
            projectInputDto.contactUrl(),
            projectInputDto.referenceUrl()
        );

        projectSkillRepository.deleteByProjectId(projectId);
        Set<ProjectSkill> projectSkills = createProjectSkills(project, projectInputDto.skills());
        projectSkillRepository.saveAll(projectSkills);

        projectRecruitmentRepository.deleteByProjectId(projectId);
        Set<ProjectRecruitment> projectRecruitments = createProjectRecruitments(
            project,
            projectInputDto.projectRecruitmentDtos()
        );
        projectRecruitmentRepository.saveAll(projectRecruitments);

        return project;
    }

    @Override
    @Transactional
    public void deleteProject(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
            new EntityNotFoundException("Project not found with id: " + projectId));
        User currentUser = authService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        projectRepository.delete(project);
        projectApplicantRepository.deleteByProjectId(projectId);
        projectBookmarkRepository.deleteByProjectId(projectId);
        projectMemberRepository.deleteByProjectId(projectId);
        projectRecruitmentRepository.deleteByProjectId(projectId);
        projectSkillRepository.deleteByProjectId(projectId);
    }

    @Override
    public Set<User> getUsersByProjectId(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));

        return project.getProjectMembers().stream()
            .map(ProjectMember::getUser)
            .sorted(Comparator
                .comparing((User user) -> user.getUserJobRoles().stream()
                    .map(userJob -> userJob.getJobRole().ordinal())
                    .min(Integer::compareTo)
                    .orElse(Integer.MAX_VALUE)) // job_id 기준 정렬, 없으면 가장 큰 값으로
                .thenComparing(User::getName)) // job_id가 같으면 이름순 정렬
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<User> getAppliedUsersByProjectId(UUID projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));

        return project.getProjectApplicants().stream()
            .map(ProjectApplicant::getUser)
            .sorted(Comparator
                .comparing((User user) -> user.getUserJobRoles().stream()
                    .map(userJob -> userJob.getJobRole().ordinal())
                    .min(Integer::compareTo)
                    .orElse(Integer.MAX_VALUE)) // job_id 기준 정렬, 없으면 가장 큰 값으로
                .thenComparing(User::getName)) // job_id가 같으면 이름순 정렬
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public Set<User> approveAppliedUser(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserInfoByUserId(userId);
        ProjectMember projectMember = ProjectMember.builder()
            .project(project)
            .user(user)
            .isLeader(false)
            .joinedAt(LocalDateTime.now())
            .build();
        project.getProjectMembers().add(projectMember);

        project.getProjectApplicants()
            .removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    @Transactional
    public Set<User> rejectAppliedUser(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserInfoByUserId(userId);
        project.getProjectApplicants()
            .removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    @Transactional
    public Set<User> rejectMemberUser(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserInfoByUserId(userId);
        project.getProjectMembers()
            .removeIf(member -> member.getUser().getId().equals(user.getId()));
        projectRepository.save(project);

        return getUsersByProjectId(projectId);
    }

    @Override
    public void delegateLeader(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        User currentUser = userService.getCurrentUser();

        if (!getLeader(project).getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        User user = userService.getUserInfoByUserId(userId);

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
    public Set<Project> getUserProjects(UUID userId) {
        User user = userService.getUserInfoByUserId(userId);
        return user.getProjectMembers().stream()
            .map(ProjectMember::getProject)
            .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void deleteUserProject(UUID projectId) {
        User user = userService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
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
    public Set<Project> getUserBookmarkProjects(UUID userId) {
        User user = userService.getUserInfoByUserId(userId);
        return user.getProjectBookmarks().stream()
            .map(ProjectBookmark::getProject)
            .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Project applyProject(UUID projectId) {
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
    public void cancelApplyProject(UUID projectId) {
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
        User user = authService.getCurrentUser();
        List<ProjectMember> projectMembers = projectMemberRepository.findByUserId(user.getId());

        return projectMembers.stream()
            .map(ProjectMember::getProject)
            .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean toggleCurrentUserBookmark(UUID projectId) {
        User user = userService.getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
        boolean isBookmarked;

        if (user.getProjectBookmarks().stream().anyMatch(
            projectBookmark -> projectBookmark.getProject().getId().equals(project.getId()))) {
            user.getProjectBookmarks().removeIf(
                projectBookmark -> projectBookmark.getProject().getId().equals(project.getId()));
            project.setBookmarkCount(project.getBookmarkCount() - 1);
            isBookmarked = false;
        } else {
            user.getProjectBookmarks().add(ProjectBookmark.builder()
                .project(project)
                .user(user)
                .build());
            project.setBookmarkCount(project.getBookmarkCount() + 1);
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

    private ProjectMember createProjectMember(Project project, User user, boolean isLeader) {
        return ProjectMember.builder()
            .project(project)
            .user(user)
            .isLeader(isLeader)
            .build();
    }

    private Set<ProjectRecruitment> createProjectRecruitments(
        Project project,
        Set<ProjectRecruitmentDto> projectRecruitmentDtos
    ) {
        return projectRecruitmentDtos.stream()
            .map(dto ->
                ProjectRecruitment.builder()
                    .project(project)
                    .jobRole(dto.jobRole())
                    .remainingCount(dto.remainingCount())
                    .currentCount(dto.currentCount())
                    .build())
            .collect(Collectors.toSet());
    }

    private Set<ProjectSkill> createProjectSkills(
        Project project,
        Set<Skill> skills
    ) {
        return skills.stream()
            .map(skill -> ProjectSkill.builder()
                .project(project)
                .skill(skill)
                .build())
            .collect(Collectors.toSet());
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
//                .job(referenceService.getJobById(jobDto.jobId()))
                .recruitmentCnt(jobDto.count())
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
//                .job(referenceService.getJobById(jobDto.jobId()))
                .memberCnt(jobDto.count())
                .build();
            projectMemberJobs.add(projectMemberJob);
        });
        return projectMemberJobs;
    }

    private Set<ProjectSkill> getProjectSkills(ProjectInputDto projectInputDto, Project project) {
        Set<ProjectSkill> projectSkills = new HashSet<>();
        projectInputDto.skills().forEach(skill -> {
            ProjectSkill projectSkill = ProjectSkill.builder()
                .project(project)
//                .skill(referenceService.getSkillById(skillId))
                .build();
            projectSkills.add(projectSkill);
        });
        return projectSkills;
    }

    private User getLeader(Project project) {
        ProjectMember leader = projectMemberRepository.findByProjectIdAndLeaderTrue(project.getId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Leader not found with project id: " + project.getId()));
        return leader.getUser();
    }
}
