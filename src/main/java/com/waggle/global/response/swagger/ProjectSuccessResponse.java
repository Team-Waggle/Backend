package com.waggle.global.response.swagger;

import com.waggle.domain.project.dto.ProjectWithUserResponseDto;
import com.waggle.global.response.SuccessResponse;

public class ProjectSuccessResponse extends SuccessResponse<ProjectWithUserResponseDto> {
    public ProjectSuccessResponse(int code, String message, ProjectWithUserResponseDto payload) {
        super(code, message, payload);
    }
}
