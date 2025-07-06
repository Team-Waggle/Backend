package com.waggle.global.response.swagger;

import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class ProjectsSuccessResponse extends SuccessResponse<List<ProjectResponseDto>> {

    public ProjectsSuccessResponse(int code, String message, List<ProjectResponseDto> payload) {
        super(code, message, payload);
    }
}
