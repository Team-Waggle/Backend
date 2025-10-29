package com.waggle.global.response.swagger;

import com.waggle.domain.project.dto.ProjectAppliedUserResponseDto;
import com.waggle.global.response.SuccessResponse;

public class ProjectAppliedUserSuccessResponse extends
    SuccessResponse<ProjectAppliedUserResponseDto> {

    public ProjectAppliedUserSuccessResponse(
        int code,
        String message,
        ProjectAppliedUserResponseDto payload
    ) {
        super(code, message, payload);
    }
}
