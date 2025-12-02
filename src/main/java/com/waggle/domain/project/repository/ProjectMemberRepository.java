package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    Optional<ProjectMember> findByProjectIdAndIsLeaderTrue(Long projectId);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, UUID userId);

    @Query("""
        SELECT pm
        FROM ProjectMember pm
        JOIN FETCH pm.project p
        JOIN FETCH p.user
        WHERE pm.user.id = :userId
        ORDER BY p.createdAt DESC
        """)
    List<ProjectMember> findByUserIdOrderByProject_CreatedAtDesc(@Param("userId") UUID userId);

    List<ProjectMember> findByProjectIdOrderByJoinedAtDesc(Long projectId);

    void deleteByProjectId(Long projectId);

    void deleteByUserId(UUID userId);

    boolean existsByProjectIdAndUserId(Long projectId, UUID userId);
}
