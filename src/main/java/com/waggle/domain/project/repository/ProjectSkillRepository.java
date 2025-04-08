package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectSkill;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, UUID> {

    void deleteByProjectId(UUID projectId);
}
