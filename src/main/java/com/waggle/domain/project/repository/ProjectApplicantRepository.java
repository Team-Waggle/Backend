package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectApplicant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectApplicantRepository extends JpaRepository<ProjectApplicant, UUID> {

    List<ProjectApplicant> findByProjectId(UUID projectId);

    void deleteByProjectId(UUID projectId);
}
