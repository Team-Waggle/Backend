package com.waggle.domain.application.repository;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("""
        SELECT a
        FROM Application a
        JOIN FETCH a.user
        JOIN FETCH a.project
        WHERE a.id = :id AND a.deletedAt IS NULL
        """)
    Optional<Application> findByIdWithRelations(@Param("id") Long id);

    @Query("""
        SELECT a
        FROM Application a
        JOIN FETCH a.user
        JOIN FETCH a.project
        WHERE a.user.id = :userId AND a.deletedAt IS NULL
        ORDER BY a.createdAt DESC, a.id DESC
        """)
    List<Application> findByUserIdWithRelations(@Param("userId") UUID userId);

    @Query("""
        SELECT a
        FROM Application a
        JOIN FETCH a.user
        JOIN FETCH a.project
        WHERE a.project.id = :projectId AND a.deletedAt IS NULL
        ORDER BY a.createdAt DESC, a.id DESC
        """)
    List<Application> findByProjectIdWithRelations(@Param("projectId") UUID projectId);

    @Query("""
        SELECT a
        FROM Application a
        JOIN FETCH a.user
        JOIN FETCH a.project
        WHERE a.status = :status AND a.user.id = :userId AND a.deletedAt IS NULL
        ORDER BY a.createdAt DESC, a.id DESC
        """)
    List<Application> findByStatusAndUserIdWithRelations(
        @Param("status") ApplicationStatus status,
        @Param("userId") UUID userId
    );

    @Query("""
        SELECT a
        FROM Application a
        JOIN FETCH a.user
        JOIN FETCH a.project
        WHERE a.status = :status AND a.project.id = :projectId AND a.deletedAt IS NULL
        ORDER BY a.createdAt DESC, a.id DESC
        """)
    List<Application> findByStatusAndProjectIdWithRelations(
        @Param("status") ApplicationStatus status,
        @Param("projectId") UUID projectId
    );

    void deleteByUserId(UUID userId);
}
