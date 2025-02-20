package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.Project;

import java.util.UUID;

public interface ProjectService {
    Project create(ProjectInputDto projectInputDto);
    Project update(UUID id, ProjectInputDto projectInputDto);
    void delete(UUID id);
    Project findById(UUID id);
}
