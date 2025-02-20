package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.*;
import com.waggle.domain.reference.entity.DurationOfWorking;
import com.waggle.domain.reference.entity.Industry;
import com.waggle.domain.reference.entity.WaysOfWorking;
import com.waggle.domain.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "프로젝트 응답 dto")
public class ProjectWithUserResponseDto {

    @Schema(description = "고유값", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @JsonProperty("title")
    private String title;

    @Schema(description = "산업 분야")
    @JsonProperty("industry")
    private Industry industry;

    @Schema(description = "진행 방식")
    @JsonProperty("ways_of_working")
    private WaysOfWorking waysOfWorking;

    @Schema(description = "마감 일자", example = "2021-07-01T00:00:00")
    @JsonProperty("recruitment_date")
    private LocalDateTime recruitmentDate;

    @Schema(description = "진행 기간")
    @JsonProperty("duration_of_working")
    private DurationOfWorking durationOfWorking;

    @Schema(description = "모집 직무 및 인원")
    @JsonProperty("recruitment_jobs")
    private Set<ProjectRecruitmentJob> recruitmentJobs;

    @Schema(description = "참여한 멤버 직무 및 인원")
    @JsonProperty("member_jobs")
    private Set<ProjectMemberJob> memberJobs;

    @Schema(description = "사용 스킬 목록")
    @JsonProperty("skills")
    private Set<ProjectSkill> projectSkills;

    @Schema(description = "소개")
    @JsonProperty("detail")
    private String detail;

    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @JsonProperty("connect_url")
    private String connectUrl;

    @Schema(description = "참조 링크", example = "www.naver.com")
    @JsonProperty("reference_url")
    private String referenceUrl;

    @Schema(description = "북마크 수", example = "0")
    @JsonProperty("bookmark_cnt")
    private int bookmarkCnt;

    @Schema(description = "참가한 사용자")
    @JsonProperty("users")
    private Set<UserResponseDto> users;

    @Schema(description = "생성 일자", example = "2001-05-21T00:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일자", example = "2025-01-19T00:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static ProjectWithUserResponseDto from(Project project) {
        return ProjectWithUserResponseDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .industry(project.getIndustry())
                .waysOfWorking(project.getWaysOfWorking())
                .recruitmentDate(project.getRecruitmentDate())
                .durationOfWorking(project.getDurationOfWorking())
                .recruitmentJobs(project.getRecruitmentJobs().stream()
                        .sorted(Comparator.comparing(prj -> prj.getJob().getId()))
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .memberJobs(project.getMemberJobs().stream()
                        .sorted(Comparator.comparing(prj -> prj.getJob().getId()))
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .projectSkills(project.getProjectSkills().stream()
                        .sorted(Comparator.comparing(prj -> prj.getSkill().getId()))
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .detail(project.getDetail())
                .connectUrl(project.getConnectUrl())
                .referenceUrl(project.getReferenceUrl())
                .bookmarkCnt(project.getBookmarkCnt())
                .users(project.getProjectUsers().stream()
                        .map(ProjectUser::getUser)
                        .map(UserResponseDto::from)
                        .sorted(Comparator.comparing(UserResponseDto::getUserJobs, Comparator.comparing(uj -> uj.iterator().next().getJob().getId()))
                                .thenComparing(UserResponseDto::getName))
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
