package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    Optional<ProjectMember> findByProjectIdAndLeaderTrue(UUID projectId);

    List<ProjectMember> findByUserId(UUID userId);

    List<ProjectMember> findByProjectId(UUID projectId);
}
