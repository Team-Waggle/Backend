package com.waggle.domain.application.service;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.application.dto.CreateApplicationRequest;
import com.waggle.domain.application.dto.UpdateStatusRequest;
import com.waggle.domain.application.repository.ApplicationRepository;
import com.waggle.domain.member.repository.MemberRepository;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.repository.ProjectV2Repository;
import com.waggle.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final ProjectV2Repository projectRepository;

    @Transactional
    public Application createApplication(
        UUID projectId,
        CreateApplicationRequest createApplicationRequest,
        User user
    ) {
        ProjectV2 project = projectRepository.findByIdWithRelations(projectId).orElseThrow(
            () -> new EntityNotFoundException("Project not found with id " + projectId)
        );

        Application application = Application.builder()
            .position(createApplicationRequest.position())
            .user(user)
            .project(project)
            .build();

        return applicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public List<Application> getProjectApplications(
        UUID projectId,
        ApplicationStatus status,
        User user
    ) {
        if (!memberRepository.existsByUserIdAndProjectId(user.getId(), projectId)) {
            throw new AccessDeniedException(
                "Access denied to applications with projectId: " + projectId
            );
        }

        if (status == null) {
            return applicationRepository.findByProjectIdWithRelations(projectId);
        }

        return applicationRepository.findByStatusAndProjectIdWithRelations(status, projectId);
    }

    @Transactional(readOnly = true)
    public List<Application> getUserApplications(
        ApplicationStatus status,
        User user
    ) {
        if (status == null) {
            return applicationRepository.findByUserIdWithRelations(user.getId());
        }

        return applicationRepository.findByStatusAndUserIdWithRelations(status, user.getId());
    }

    @Transactional
    public Application updateApplicationStatus(
        Long applicationId,
        UpdateStatusRequest updateStatusRequest,
        User user
    ) {
        Application application = applicationRepository.findByIdWithRelations(applicationId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Application not found with id " + applicationId)
            );

        switch (updateStatusRequest.status()) {
            case APPROVED -> approveApplication(application, user);
            case REJECTED -> rejectApplication(application, user);
            case CANCELLED -> cancelApplication(application, user);
            default -> throw new IllegalStateException(
                "Unexpected value: " + updateStatusRequest.status());
        }

        return application;
    }

    private void approveApplication(Application application, User user) {
        ProjectV2 project = application.getProject();

        if (!project.getLeader().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to project with id: " + project.getId());
        }

        application.approve();
    }

    private void rejectApplication(Application application, User user) {
        ProjectV2 project = application.getProject();

        if (!project.getLeader().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to project with id: " + project.getId());
        }

        application.reject();
    }

    private void cancelApplication(Application application, User user) {
        if (!application.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(
                "Access denied to application with id: " + application.getId()
            );
        }

        application.cancel();
    }
}
