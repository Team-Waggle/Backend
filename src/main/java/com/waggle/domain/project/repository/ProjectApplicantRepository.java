package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.reference.enums.ApplicationStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectApplicantRepository extends JpaRepository<ProjectApplicant, UUID> {

    Optional<ProjectApplicant> findByProjectIdAndUserId(Long projectId, UUID userId);

    List<ProjectApplicant> findByProjectId(Long projectId);

    List<ProjectApplicant> findByUserId(UUID userId);

    void deleteByProjectId(Long projectId);

    boolean existsByProjectIdAndUserIdAndStatusNot(
        Long projectId,
        UUID userId,
        ApplicationStatus status
    );
}
