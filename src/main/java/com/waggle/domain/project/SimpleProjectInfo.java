package com.waggle.domain.project;

import com.waggle.domain.project.entity.Project;

public record SimpleProjectInfo(
    Long id,
    String title,
    String detail
) {

    public static SimpleProjectInfo from(Project project) {
        return new SimpleProjectInfo(project.getId(), project.getTitle(), project.getDetail());
    }
}
