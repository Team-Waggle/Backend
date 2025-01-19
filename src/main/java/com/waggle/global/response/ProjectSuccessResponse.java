package com.waggle.global.response;

import com.waggle.domain.project.entity.Project;

public class ProjectSuccessResponse extends SuccessResponse<Project> {
    public ProjectSuccessResponse(int code, String message, Project payload) {
        super(code, message, payload);
    }
}
