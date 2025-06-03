package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.reference.enums.ApplicationStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectApplicantRepository extends JpaRepository<ProjectApplicant, UUID> {

    Optional<ProjectApplicant> findByProjectIdAndUserId(UUID projectId, UUID userId);

    List<ProjectApplicant> findByProjectIdOrderByAppliedAtDesc(UUID projectId);

    List<ProjectApplicant> findByUserIdOrderByAppliedAtDesc(UUID userId);

    void deleteByProjectId(UUID projectId);

    boolean existsByProjectIdAndUserIdAndStatusNot(
        UUID projectId,
        UUID userId,
        ApplicationStatus status
    );
}
