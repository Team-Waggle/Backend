package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectRecruitment;
import com.waggle.domain.reference.enums.JobRole;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRecruitmentRepository extends JpaRepository<ProjectRecruitment, UUID> {

    Optional<ProjectRecruitment> findByProjectIdAndJobRole(Long projectId, JobRole jobRole);

    List<ProjectRecruitment> findByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);
}
