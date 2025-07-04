package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    Optional<ProjectMember> findByProjectIdAndIsLeaderTrue(Long projectId);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, UUID userId);

    List<ProjectMember> findByUserIdOrderByProject_CreatedAtDesc(UUID userId);

    List<ProjectMember> findByProjectIdOrderByJoinedAtDesc(Long projectId);

    void deleteByProjectId(Long projectId);

    boolean existsByProjectIdAndUserId(Long projectId, UUID userId);
}
