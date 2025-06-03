package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectBookmark;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectBookmarkRepository extends JpaRepository<ProjectBookmark, UUID> {

    List<ProjectBookmark> findByUserIdOrderByProject_CreatedAtDesc(UUID userId);

    List<ProjectBookmark> findByProjectId(UUID projectId);

    void deleteByProjectId(UUID projectId);

    void deleteByProjectIdAndUserId(UUID projectId, UUID userId);

    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);
}
