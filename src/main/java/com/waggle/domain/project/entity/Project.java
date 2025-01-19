package com.waggle.domain.project.entity;

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
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "프로젝트 모집글 엔티티")
public class Project {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Schema(description = "고유값", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @Column(nullable = false)
    private String title; //제목

    @Schema(description = "마감 일자", example = "2021-07-01T00:00:00")
    @Column(name = "recruitment_date", nullable = false)
    private LocalDateTime recruitmentDate; //프로젝트 모집 마감 일자

    @Schema(description = "소개", example = "기본적으로 Spring을 쓰실 줄 알며, RestAPI를 잘 쓰시는 분을 모집합니다.")
    @Column(length = 1000)
    private String detail; //소개

    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @Column(name = "connect_url")
    private String connectUrl; //연락 링크

    @Schema(description = "참조 링크", example = "www.naver.com")
    @Column(name = "reference_url")
    private String referenceUrl; //참고 링크

    @Schema(description = "북마크 수(스크랩)", example = "0")
    @Column(name = "bookmark_cnt")
    private int bookmarkCnt; //북마크 수(스크랩)

    @Schema(description = "생성 일자", example = "2001-05-21T00:00:00")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; //생성일자

    @Schema(description = "수정 일자", example = "2025-01-19T00:00:00")
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //수정일자

    //cascade=강제삭제방식
    //mappedBy=연결된 필드 변수 명
    //set=배열(gpt 추천)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectSkill> projectSkills;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectJob> projectJobs;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @ManyToOne
    @JoinColumn(name = "wow_id")
    private WaysOfWorking waysOfWorking;

    @ManyToOne
    @JoinColumn(name = "dow_id")
    private DurationOfWorking durationOfWorking;
}