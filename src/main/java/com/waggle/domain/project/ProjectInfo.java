package com.waggle.domain.project;

import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.project.entity.ProjectMember;
import com.waggle.domain.project.entity.ProjectRecruitment;
import com.waggle.domain.project.entity.ProjectSkill;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectInfo(
    Project project,
    List<ProjectSkill> projectSkills,
    List<ProjectMember> projectMembers,
    List<ProjectApplicant> projectApplicants,
    List<ProjectRecruitment> projectRecruitments
) {

    public static ProjectInfo of(
        Project project,
        List<ProjectSkill> projectSkills,
        List<ProjectMember> projectMembers,
        List<ProjectApplicant> projectApplicants,
        List<ProjectRecruitment> projectRecruitments
    ) {
        return new ProjectInfo(
            project,
            projectSkills,
            projectMembers,
            projectApplicants,
            projectRecruitments
        );
    }
}
