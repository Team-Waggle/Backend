package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectBookmark;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectBookmarkRepository extends JpaRepository<ProjectBookmark, UUID> {

    @Query("""
        SELECT m FROM ProjectBookmark m
        JOIN FETCH m.project p
        JOIN FETCH p.user
        WHERE m.user.id = :userId
        AND (:cursor IS NULL OR
             (m.project.createdAt < :cursorCreatedAt OR
              (m.project.createdAt = :cursorCreatedAt AND m.id < :cursor)))
        ORDER BY m.project.createdAt DESC, m.id DESC
        """)
    List<ProjectBookmark> findByUserIdWithCursor(
        @Param("userId") UUID userId,
        @Param("cursor") Long cursor,
        @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
        Pageable pageable
    );

    List<ProjectBookmark> findByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);

    void deleteByProjectIdAndUserId(Long projectId, UUID userId);

    boolean existsByProjectIdAndUserId(Long projectId, UUID userId);
}
