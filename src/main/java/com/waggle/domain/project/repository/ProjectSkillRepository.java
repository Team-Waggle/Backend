package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectSkill;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, UUID> {

    List<ProjectSkill> findByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);
}
