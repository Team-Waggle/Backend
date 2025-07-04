package com.waggle.global.response.swagger;

import com.waggle.domain.projectV2.dto.ProjectResponse;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class ProjectV2sSuccessResponse extends SuccessResponse<List<ProjectResponse>> {

    public ProjectV2sSuccessResponse(int code, String message, List<ProjectResponse> payload) {
        super(code, message, payload);
    }
}
