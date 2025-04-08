package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectRecruitment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRecruitmentRepository extends JpaRepository<ProjectRecruitment, UUID> {

    List<ProjectRecruitment> findByProjectId(UUID projectId);

    void deleteByProjectId(UUID projectId);
}
