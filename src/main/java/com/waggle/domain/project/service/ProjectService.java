package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.CreateProjectDto;
import com.waggle.domain.project.entity.Project;

public interface ProjectService {
    Project create(CreateProjectDto createProjectDto);
}
