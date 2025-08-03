package com.waggle.domain.project.dto;

import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.reference.enums.WorkPeriod;
import com.waggle.domain.reference.enums.WorkWay;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "프로젝트 필터 조건")
public record ProjectFilterDto(
    @Parameter(
        description = "직무 필터",
        example = "BACKEND,FRONTEND",
        schema = @Schema(allowableValues = {"FRONTEND", "BACKEND", "DESIGNER", "IOS", "ANDROID",
            "DEVOPS", "PLANNER", "MARKETER"}
        )
    )
    Set<Position> positions,

    @Parameter(
        description = "스킬 필터",
        example = "JAVA,SPRING,REACT",
        schema = @Schema(allowableValues = {"JAVA", "JAVASCRIPT", "TYPESCRIPT", "NODE_JS",
            "NEXT_JS", "NEST_JS", "SVELTE", "VUE", "REACT", "SPRING", "GO", "KOTLIN", "EXPRESS",
            "MYSQL", "MONGODB", "PYTHON", "DJANGO", "PHP", "GRAPHQL", "FIREBASE", "FLUTTER",
            "SWIFT", "REACT_NATIVE", "UNITY", "AWS", "KUBERNETES", "DOCKER", "GIT", "FIGMA",
            "XD", "ZEPLIN", "JEST", "AXURE", "MS_OFFICE", "ILLUSTRATOR", "PHOTOSHOP", "INDESIGN",
            "PREMIERE_PRO", "AFTER_EFFECTS", "MAX_3D", "BLENDER", "CINEMA_4D"}
        )
    )
    Set<Skill> skills,

    @Parameter(
        description = "산업 분야 필터",
        example = "FINANCE,ECOMMERCE",
        schema = @Schema(allowableValues = {"FINANCE", "REAL_ESTATE", "INTERIOR",
            "MEDICAL_HEALTHCARE", "ECOMMERCE", "ENTERTAINMENT", "TRAVEL", "SOCIAL_NETWORK",
            "CULTURE_ART", "BEAUTY_FASHION", "RELIGION", "SALES_DISTRIBUTION", "EDUCATION",
            "CONSTRUCTION", "HEALTH", "PARENTING", "MEDIA_ADVERTISING"}
        )
    )
    Set<Industry> industries,

    @Parameter(
        description = "진행 기간 필터",
        example = "THREE_MONTHS,SIX_MONTHS",
        schema = @Schema(allowableValues = {"ONE_MONTH", "TWO_MONTHS", "THREE_MONTHS",
            "FOUR_MONTHS", "FIVE_MONTHS", "SIX_MONTHS", "OVER_SIX_MONTHS"}
        )
    )
    Set<WorkPeriod> workPeriods,

    @Parameter(
        description = "진행 방식 필터",
        example = "ONLINE,ONLINE_OFFLINE",
        schema = @Schema(allowableValues = {"ONLINE", "OFFLINE", "ONLINE_OFFLINE"}
        )
    )
    Set<WorkWay> workWays
) {

}
