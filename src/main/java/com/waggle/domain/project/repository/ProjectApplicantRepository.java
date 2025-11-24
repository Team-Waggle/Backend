package com.waggle.domain.project.repository;

import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.project.entity.ProjectApplicant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectApplicantRepository extends JpaRepository<ProjectApplicant, UUID> {

    Optional<ProjectApplicant> findByProjectIdAndUserId(Long projectId, UUID userId);

    Optional<ProjectApplicant> findByProjectIdAndUserIdAndStatusNot(
        Long projectId,
        UUID userId,
        ApplicationStatus status
    );

    List<ProjectApplicant> findByProjectIdAndStatusNotInOrderByAppliedAtDesc(
        Long projectId,
        List<ApplicationStatus> status
    );


    @Query("""
        SELECT pa FROM ProjectApplicant pa
        JOIN FETCH pa.project p
        JOIN FETCH p.user
        WHERE pa.user.id = :userId AND pa.status NOT IN :excludedStatuses
        ORDER BY pa.appliedAt DESC
        """)
    List<ProjectApplicant> findByUserIdWithRelationsOrderByAppliedAtDesc(
        @Param("userId") UUID userId,
        @Param("excludedStatuses") List<ApplicationStatus> excludedStatuses
    );

    @Query("""
        SELECT pa FROM ProjectApplicant pa
        JOIN FETCH pa.project p
        JOIN FETCH p.user
        WHERE pa.user.id = :userId
        AND pa.status = :status
        ORDER BY pa.appliedAt DESC
        """)
    List<ProjectApplicant> findByUserIdAndStatusWithRelationsOrderByAppliedAtDesc(
        @Param("userId") UUID userId,
        @Param("status") ApplicationStatus status
    );

    void deleteByProjectId(Long projectId);

    boolean existsByProjectIdAndUserIdAndStatusNot(
        Long projectId,
        UUID userId,
        ApplicationStatus status
    );

    boolean existsByProjectIdAndUserIdAndStatus(
        Long projectId,
        UUID userId,
        ApplicationStatus status
    );
}
