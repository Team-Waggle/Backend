package com.waggle.domain.project.service;

import com.waggle.domain.notification.NotificationType;
import com.waggle.domain.notification.dto.NotificationRequestDto;
import com.waggle.domain.notification.service.NotificationService;
import com.waggle.domain.project.ProjectInfo;
import com.waggle.domain.project.dto.ProjectApplicationDto;
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
import com.waggle.domain.reference.enums.ApplicationStatus;
import com.waggle.domain.reference.enums.JobRole;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.exception.AccessDeniedException;
import com.waggle.global.exception.ProjectException;
import com.waggle.global.response.ApiStatus;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final NotificationService notificationService;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectApplicantRepository projectApplicantRepository;
    private final ProjectBookmarkRepository projectBookmarkRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRecruitmentRepository projectRecruitmentRepository;
    private final ProjectSkillRepository projectSkillRepository;

    @Override
    @Transactional
    public Project createProject(ProjectInputDto projectInputDto, User user) {
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

        ProjectMember projectMember = createProjectMember(project, user, JobRole.PLANNER, true);
        projectMemberRepository.save(projectMember);

        List<ProjectSkill> projectSkills = createProjectSkills(project, projectInputDto.skills());
        projectSkillRepository.saveAll(projectSkills);

        List<ProjectRecruitment> projectRecruitments = createProjectRecruitments(
            project,
            projectInputDto.projectRecruitmentDtos()
        );
        projectRecruitmentRepository.saveAll(projectRecruitments);

        return project;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Project> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

    }

    @Override
    @Transactional(readOnly = true)
    public ProjectInfo getProjectInfoByProject(Project project) {
        List<ProjectSkill> projectSkills = projectSkillRepository.findByProjectId(project.getId());
        List<ProjectMember> projectMembers =
            projectMemberRepository.findByProjectIdOrderByJoinedAtDesc(project.getId());
        List<ProjectApplicant> projectApplicants =
            projectApplicantRepository.findByProjectIdOrderByAppliedAtDesc(project.getId());
        List<ProjectRecruitment> projectRecruitments =
            projectRecruitmentRepository.findByProjectId(project.getId());

        return ProjectInfo.of(
            project,
            projectSkills,
            projectMembers,
            projectApplicants,
            projectRecruitments
        );
    }

    @Override
    @Transactional
    public Project updateProject(Long projectId, ProjectInputDto projectInputDto, User user) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

        if (!getLeaderByProjectId(project.getId()).getId().equals(user.getId())) {
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
        List<ProjectSkill> projectSkills = createProjectSkills(project, projectInputDto.skills());
        projectSkillRepository.saveAll(projectSkills);

        projectRecruitmentRepository.deleteByProjectId(projectId);
        List<ProjectRecruitment> projectRecruitments = createProjectRecruitments(
            project,
            projectInputDto.projectRecruitmentDtos()
        );
        projectRecruitmentRepository.saveAll(projectRecruitments);

        return project;
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

        if (!getLeaderByProjectId(project.getId()).getId().equals(user.getId())) {
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
    @Transactional(readOnly = true)
    public List<User> getUsersByProjectId(Long projectId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findByProjectIdOrderByJoinedAtDesc(
            projectId);

        return projectMembers.stream()
            .map(ProjectMember::getUser)
            .toList();
    }

    @Override
    public List<User> getAppliedUsersByProjectId(Long projectId) {
        List<ProjectApplicant> projectApplicants =
            projectApplicantRepository.findByProjectIdOrderByAppliedAtDesc(projectId);

        return projectApplicants.stream()
            .map(ProjectApplicant::getUser)
            .toList();
    }

    @Override
    @Transactional
    public List<User> approveAppliedUser(Long projectId, UUID userId, User user) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

        if (!getLeaderByProjectId(project.getId()).getId().equals(user.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        ProjectApplicant projectApplicant = projectApplicantRepository
            .findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Applicant not found for project id: " + projectId + " and user id: " + userId));
        projectApplicant.updateStatus(ApplicationStatus.APPROVED);

        JobRole jobRole = projectApplicant.getJobRole();
        ProjectRecruitment recruitment = projectRecruitmentRepository
            .findByProjectIdAndJobRole(projectId, jobRole)
            .orElseThrow(() -> new EntityNotFoundException(
                "Recruitment not found for project id: " + projectId + "and job role: " + jobRole));
        recruitment.addMember();

        ProjectMember member = createProjectMember(
            project,
            projectApplicant.getUser(),
            jobRole,
            false
        );
        projectMemberRepository.save(member);

        notificationService.createNotification(
            NotificationRequestDto.of(
                NotificationType.APPLICATION_ACCEPTED,
                "/projects/" + projectId,
                project.getTitle()
            ),
            userService.getUserById(userId)
        );

        return getUsersByProjectId(projectId);
    }

    @Override
    @Transactional
    public List<User> rejectAppliedUser(Long projectId, UUID userId, User user) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

        if (!getLeaderByProjectId(project.getId()).getId().equals(user.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        ProjectApplicant projectApplicant = projectApplicantRepository
            .findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Applicant not found for project id: " + projectId + " and user id: " + userId));
        projectApplicant.updateStatus(ApplicationStatus.REJECTED);

        notificationService.createNotification(
            NotificationRequestDto.of(
                NotificationType.APPLICATION_REJECTED,
                "/projects/",
                project.getTitle()
            ),
            userService.getUserById(userId)
        );

        return getUsersByProjectId(projectId);
    }

    @Override
    @Transactional
    public List<User> removeMemberUser(Long projectId, UUID userId, User user) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

        if (!getLeaderByProjectId(project.getId()).getId().equals(user.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        ProjectMember projectMember = projectMemberRepository
            .findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Member not found for project id: " + projectId + " and user id: " + userId));

        if (projectMember.isLeader()) {
            throw new IllegalStateException("Cannot remove the project leader");
        }

        JobRole jobRole = projectMember.getJobRole();
        ProjectRecruitment recruitment = projectRecruitmentRepository
            .findByProjectIdAndJobRole(projectId, jobRole)
            .orElseThrow(() -> new EntityNotFoundException(
                "Recruitment not found for project id: " + projectId + "and job role: " + jobRole));
        recruitment.removeMember();

        projectMemberRepository.delete(projectMember);

        return getUsersByProjectId(projectId);
    }

    @Override
    @Transactional
    public void delegateLeader(Long projectId, UUID userId, User user) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

        if (!getLeaderByProjectId(project.getId()).getId().equals(user.getId())) {
            throw new AccessDeniedException(ApiStatus._NOT_LEADER);
        }

        ProjectMember leader = projectMemberRepository
            .findByProjectIdAndUserId(projectId, user.getId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Member not found for project id: " + projectId + " and user id: "
                    + user.getId()));
        ProjectMember member = projectMemberRepository
            .findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Member not found for project id: " + projectId + " and user id: " + userId));

        leader.makeMember();
        member.makeLeader();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getUserProjects(UUID userId) {
        return projectMemberRepository.findByUserIdOrderByProject_CreatedAtDesc(userId).stream()
            .map(ProjectMember::getProject)
            .toList();
    }

    @Override
    @Transactional
    public void withdrawFromProject(Long projectId, User user) {
        ProjectMember member = projectMemberRepository
            .findByProjectIdAndUserId(projectId, user.getId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Member not found for project id: " + projectId + " and user id: " + user.getId()));

        // TODO: 자동 리더 위임 로직 구현
        if (member.isLeader()) {
            throw new IllegalStateException("Leader cannot withdraw from the project");
        }

        projectMemberRepository.delete(member);

        JobRole jobRole = member.getJobRole();
        ProjectRecruitment recruitment = projectRecruitmentRepository
            .findByProjectIdAndJobRole(projectId, jobRole)
            .orElseThrow(() -> new EntityNotFoundException(
                "Recruitment not found for project id: " + projectId + "and job role: " + jobRole));
        recruitment.removeMember();
    }

    @Override
    @Transactional
    public Project applyProject(
        Long projectId,
        ProjectApplicationDto projectApplicationDto,
        User user
    ) {
        JobRole jobRole = projectApplicationDto.jobRole();
        ProjectRecruitment recruitment = projectRecruitmentRepository
            .findByProjectIdAndJobRole(projectId, jobRole)
            .orElseThrow(() -> new EntityNotFoundException(
                "Recruitment not found for project id: " + projectId + "and job role: " + jobRole));

        if (!recruitment.isRecruitable()) {
            throw new IllegalStateException(
                "Not allowed to apply the project for job role: " + jobRole);
        }

        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            throw new ProjectException(ApiStatus._ALREADY_JOINED_PROJECT);
        }

        if (projectApplicantRepository
            .existsByProjectIdAndUserIdAndStatusNot(
                projectId,
                user.getId(),
                ApplicationStatus.CANCELLED
            )
        ) {
            throw new ProjectException(ApiStatus._ALREADY_APPLIED_PROJECT);
        }

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));
        ProjectApplicant applicant = ProjectApplicant.builder()
            .project(project)
            .user(user)
            .jobRole(jobRole)
            .status(ApplicationStatus.PENDING)
            .build();
        projectApplicantRepository.save(applicant);

        notificationService.createNotification(
            NotificationRequestDto.of(
                NotificationType.APPLICATION_RECEIVED,
                "/projects/" + project.getId() + "/applications",
                user.getName(),
                project.getTitle()
            ),
            getLeaderByProjectId(projectId)
        );

        return project;
    }

    @Override
    @Transactional
    public void cancelProjectApplication(Long projectId, User user) {
        ProjectApplicant applicant = projectApplicantRepository
            .findByProjectIdAndUserId(projectId, user.getId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Applicant not found for project id: " + projectId + " and user id: "
                    + user.getId()));
        applicant.updateStatus(ApplicationStatus.CANCELLED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getAppliedProjects(User user) {
        List<ProjectApplicant> projectApplicants = projectApplicantRepository.findByUserIdOrderByAppliedAtDesc(
            user.getId());

        return projectApplicants.stream()
            .map(ProjectApplicant::getProject)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getCurrentUserProjects(User user) {
        List<ProjectMember> projectMembers = projectMemberRepository.findByUserIdOrderByProject_CreatedAtDesc(
            user.getId());

        return projectMembers.stream()
            .map(ProjectMember::getProject)
            .toList();
    }

    // TODO: 동시성 처리 필요
    @Override
    @Transactional
    public boolean toggleCurrentUserBookmark(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project not found with id: " + projectId));

        if (projectBookmarkRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            projectBookmarkRepository.deleteByProjectIdAndUserId(projectId, user.getId());
            project.decrementBookmarkCount();
            return false;
        } else {
            ProjectBookmark bookmark = ProjectBookmark.builder()
                .project(project)
                .user(user)
                .build();
            projectBookmarkRepository.save(bookmark);
            project.incrementBookmarkCount();
            return true;
        }
    }

    @Override
    public List<Project> getUserBookmarkProjects(UUID userId, Long cursor, int size) {
        LocalDateTime cursorCreatedAt = null;

        if (cursor != null) {
            cursorCreatedAt = projectRepository.findCreatedAtById(cursor).orElse(null);
        }

        Pageable pageable = PageRequest.of(0, size + 1);

        return projectBookmarkRepository.findByUserIdWithCursor(
                userId,
                cursor,
                cursorCreatedAt,
                pageable
            )
            .stream()
            .map(ProjectBookmark::getProject)
            .toList();
    }

    private ProjectMember createProjectMember(
        Project project,
        User user,
        JobRole jobRole,
        boolean isLeader
    ) {
        return ProjectMember.builder()
            .project(project)
            .user(user)
            .jobRole(jobRole)
            .isLeader(isLeader)
            .build();
    }

    private List<ProjectRecruitment> createProjectRecruitments(
        Project project,
        List<ProjectRecruitmentDto> projectRecruitmentDtos
    ) {
        return projectRecruitmentDtos.stream()
            .map(dto ->
                ProjectRecruitment.builder()
                    .project(project)
                    .jobRole(dto.jobRole())
                    .remainingCount(dto.remainingCount())
                    .currentCount(dto.currentCount())
                    .build()
            )
            .toList();
    }

    private List<ProjectSkill> createProjectSkills(
        Project project,
        List<Skill> skills
    ) {
        return skills.stream()
            .map(skill -> ProjectSkill.builder()
                .project(project)
                .skill(skill)
                .build()
            )
            .toList();
    }

    private User getLeaderByProjectId(Long projectId) {
        ProjectMember leader = projectMemberRepository.findByProjectIdAndIsLeaderTrue(projectId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Leader not found with project id: " + projectId));
        return leader.getUser();
    }
}
