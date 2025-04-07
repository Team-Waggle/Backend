package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectBookmark;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectBookmarkRepository extends JpaRepository<ProjectBookmark, UUID> {

}
