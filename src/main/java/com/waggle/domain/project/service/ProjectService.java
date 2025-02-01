package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectDto;
import com.waggle.domain.project.entity.Project;

import java.util.UUID;

public interface ProjectService {
    Project create(ProjectDto projectDto);
    Project update(UUID id, ProjectDto projectDto);
    void delete(UUID id);
    Project findById(UUID id);
}
