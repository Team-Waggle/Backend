package com.waggle.global.response.swagger;

import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.global.response.SuccessResponse;

import java.util.Set;

public class ProjectsSuccessResponse extends SuccessResponse<Set<ProjectResponseDto>> {
    public ProjectsSuccessResponse(int code, String message, Set<ProjectResponseDto> payload) {
        super(code, message, payload);
    }
}
