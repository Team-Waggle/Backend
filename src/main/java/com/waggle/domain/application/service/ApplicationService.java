package com.waggle.domain.application.service;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.application.dto.UpdateStatusDto;
import com.waggle.domain.application.repository.ApplicationRepository;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public List<Application> getMyApplications(
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
        UpdateStatusDto updateStatusDto,
        User user
    ) {
        Application application = applicationRepository.findByIdWithRelations(applicationId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Application not found with id " + applicationId)
            );

        switch (updateStatusDto.status()) {
            case APPROVED -> approveApplication(application, user);
            case REJECTED -> rejectApplication(application, user);
            case CANCELLED -> cancelApplication(application, user);
            default ->
                throw new IllegalStateException("Unexpected value: " + updateStatusDto.status());
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
