package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectBookmark;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectBookmarkRepository extends JpaRepository<ProjectBookmark, UUID> {

    List<ProjectBookmark> findByUserId(UUID userId);

    List<ProjectBookmark> findByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);

    void deleteByProjectIdAndUserId(Long projectId, UUID userId);

    boolean existsByProjectIdAndUserId(Long projectId, UUID userId);
}
