package com.waggle.global.response.swagger;

import com.waggle.domain.projectV2.dto.ProjectResponse;
import com.waggle.global.response.SuccessResponse;

public class ProjectV2SuccessResponse extends SuccessResponse<ProjectResponse> {

    public ProjectV2SuccessResponse(int code, String message, ProjectResponse payload) {
        super(code, message, payload);
    }
}
