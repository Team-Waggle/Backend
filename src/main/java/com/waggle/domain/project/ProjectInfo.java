package com.waggle.domain.project;

import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.project.entity.ProjectMember;
import com.waggle.domain.project.entity.ProjectRecruitment;
import com.waggle.domain.project.entity.ProjectSkill;
import com.waggle.domain.user.UserInfo;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectInfo(
    @Nullable UserInfo userInfo,
    Boolean bookmarked,
    Boolean applied,
    Project project,
    List<ProjectSkill> projectSkills,
    List<ProjectMember> projectMembers,
    List<ProjectApplicant> projectApplicants,
    List<ProjectRecruitment> projectRecruitments
) {

    public static ProjectInfo of(
        @Nullable UserInfo userInfo,
        Boolean bookmarked,
        Boolean applied,
        Project project,
        List<ProjectSkill> projectSkills,
        List<ProjectMember> projectMembers,
        List<ProjectApplicant> projectApplicants,
        List<ProjectRecruitment> projectRecruitments
    ) {
        return new ProjectInfo(
            userInfo,
            bookmarked,
            applied,
            project,
            projectSkills,
            projectMembers,
            projectApplicants,
            projectRecruitments
        );
    }
}
