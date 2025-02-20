package com.waggle.global.response.swagger;

import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.global.response.SuccessResponse;

public class ProjectSuccessResponse extends SuccessResponse<ProjectResponseDto> {
    public ProjectSuccessResponse(int code, String message, ProjectResponseDto payload) {
        super(code, message, payload);
    }
}
