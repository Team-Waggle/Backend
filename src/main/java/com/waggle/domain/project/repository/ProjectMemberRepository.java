package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    Optional<ProjectMember> findByProjectIdAndIsLeaderTrue(UUID projectId);

    Optional<ProjectMember> findByProjectIdAndUserId(UUID projectId, UUID userId);

    List<ProjectMember> findByUserIdOrderByProject_CreatedAtDesc(UUID userId);

    List<ProjectMember> findByProjectIdOrderByJoinedAtDesc(UUID projectId);

    void deleteByProjectId(UUID projectId);

    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);
}
