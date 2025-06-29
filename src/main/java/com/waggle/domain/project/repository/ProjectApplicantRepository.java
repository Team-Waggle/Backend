package com.waggle.domain.project.repository;

import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.project.entity.ProjectApplicant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectApplicantRepository extends JpaRepository<ProjectApplicant, UUID> {

    Optional<ProjectApplicant> findByProjectIdAndUserId(Long projectId, UUID userId);

    List<ProjectApplicant> findByProjectIdOrderByAppliedAtDesc(Long projectId);

    List<ProjectApplicant> findByUserIdOrderByAppliedAtDesc(UUID userId);

    void deleteByProjectId(Long projectId);

    boolean existsByProjectIdAndUserIdAndStatusNot(
        Long projectId,
        UUID userId,
        ApplicationStatus status
    );
}
