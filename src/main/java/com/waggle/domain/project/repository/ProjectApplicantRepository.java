package com.waggle.domain.project.repository;

import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.reference.enums.Position;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectApplicantRepository extends JpaRepository<ProjectApplicant, UUID> {

    int countByProjectIdAndStatusNotIn(Long projectId, List<ApplicationStatus> statuses);

    @Query("""
        SELECT pa.position, COUNT(pa)
        FROM ProjectApplicant pa
        WHERE pa.project.id = :projectId
        AND pa.status NOT IN :excludedStatuses
        GROUP BY pa.position
        """)
    List<Object[]> countByProjectIdAndStatusNotInGroupByPosition(
        @Param("projectId") Long projectId,
        @Param("excludedStatuses") List<ApplicationStatus> excludedStatuses
    );

    Optional<ProjectApplicant> findByProjectIdAndUserId(Long projectId, UUID userId);

    Optional<ProjectApplicant> findByProjectIdAndUserIdAndStatusNot(
        Long projectId,
        UUID userId,
        ApplicationStatus statuses
    );

    List<ProjectApplicant> findByProjectIdAndStatusNotInOrderByAppliedAtDesc(
        Long projectId,
        List<ApplicationStatus> statuses
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
