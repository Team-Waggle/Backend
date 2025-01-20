package com.waggle.global.response.swagger;

import com.waggle.domain.project.entity.Project;
import com.waggle.global.response.SuccessResponse;

public class ProjectSuccessResponse extends SuccessResponse<Project> {
    public ProjectSuccessResponse(int code, String message, Project payload) {
        super(code, message, payload);
    }
}
