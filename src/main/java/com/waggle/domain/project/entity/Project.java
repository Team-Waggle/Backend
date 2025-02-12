package com.waggle.domain.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.entity.Industry;
import com.waggle.domain.reference.entity.WaysOfWorking;
import com.waggle.domain.reference.entity.DurationOfWorking;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter //getter: 값을 가져올 수 있게 해주는 것, setter: 값을 수정할 수 있게 해주는 것
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "projects")
@Schema(description = "프로젝트 모집글 정보")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @Schema(description = "고유값", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @JsonProperty("title")
    private String title; //제목

    @ManyToOne
    @JoinColumn(name = "industry_id")
    @JsonProperty("industry")
    private Industry industry; //산업 분야

    @ManyToOne
    @JoinColumn(name = "wow_id")
    @JsonProperty("way_of_working")
    private WaysOfWorking waysOfWorking; //진행 방식

    @Column(name = "recruitment_date", nullable = false)
    @Schema(description = "마감 일자", example = "2021-07-01T00:00:00")
    @JsonProperty("recruitment_date")
    private LocalDateTime recruitmentDate; //프로젝트 모집 마감 일자

    @ManyToOne
    @JoinColumn(name = "dow_id")
    @JsonProperty("duration_of_working")
    private DurationOfWorking durationOfWorking; //진행 기간

    //cascade=강제삭제방식
    //mappedBy=연결된 필드 변수 명
    //set=배열(gpt 추천)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("recruitment_jobs")
    private Set<ProjectRecruitmentJob> recruitmentJobs; //모집 직무

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("member_jobs")
    private Set<ProjectMemberJob> memberJobs; //멤버 직무

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("skills")
    private Set<ProjectSkill> projectSkills; //사용 스킬

    @Column(length = 1000)
    @Schema(description = "소개", example = "기본적으로 Spring을 쓰실 줄 알며, RestAPI를 잘 쓰시는 분을 모집합니다.")
    @JsonProperty("detail")
    private String detail; //소개

    @Column(name = "connect_url")
    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @JsonProperty("connect_url")
    private String connectUrl; //연락 링크

    @Column(name = "reference_url")
    @Schema(description = "참조 링크", example = "www.naver.com")
    @JsonProperty("reference_url")
    private String referenceUrl; //참고 링크

    @Column(name = "bookmark_cnt")
    @Schema(description = "북마크 수(스크랩)", example = "0")
    @JsonProperty("bookmark_cnt")
    private int bookmarkCnt; //북마크 수(스크랩)

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "참여한 사용자 정보")
    @JsonProperty("users")
    @JsonIgnoreProperties("project")
    private Set<ProjectUser> projectUsers; //참여자

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    @Schema(description = "생성 일자", example = "2001-05-21T00:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt; //생성일자

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "수정 일자", example = "2025-01-19T00:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt; //수정일자
}