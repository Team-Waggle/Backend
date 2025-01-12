package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.CreateProjectDto;
import com.waggle.domain.project.dto.UpdateProjectDto;
import com.waggle.domain.project.entity.Project;

import java.util.UUID;

public interface ProjectService {
    Project create(CreateProjectDto createProjectDto);
    Project update(UUID id, UpdateProjectDto updateProjectDto);
    void delete(UUID id);
    Project findById(UUID id);
}
