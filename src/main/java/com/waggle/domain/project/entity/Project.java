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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false)
    private String title; //제목

    @ManyToOne
    private Industry industry; //산업 분야

    @ManyToOne
    private WaysOfWorking waysOfWorking; //진행 방식

    @Column(name = "recruitment_date", nullable = false)
    private LocalDateTime recruitmentDate; //프로젝트 모집 마감 일자

    @ManyToOne
    @JoinColumn(name = "dow_id")
    private DurationOfWorking durationOfWorking; //진행 기간

    //cascade=강제삭제방식
    //mappedBy=연결된 필드 변수 명
    //set=배열(gpt 추천)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProjectRecruitmentJob> recruitmentJobs; //모집 직무

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProjectMemberJob> memberJobs; //멤버 직무

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProjectSkill> projectSkills; //사용 스킬

    @Column(length = 1000)
    @Schema(description = "소개", example = "기본적으로 Spring을 쓰실 줄 알며, RestAPI를 잘 쓰시는 분을 모집합니다.")
    private String detail; //소개

    @Column(name = "connect_url")
    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    private String connectUrl; //연락 링크

    @Column(name = "reference_url")
    @Schema(description = "참조 링크", example = "www.naver.com")
    private String referenceUrl; //참고 링크

    @Column(name = "bookmark_cnt")
    @Schema(description = "북마크 수(스크랩)", example = "0")
    private int bookmarkCnt; //북마크 수(스크랩)

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    //@JsonIgnoreProperties("project")
    private Set<ProjectUser> projectUsers; //참여자

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; //생성일자

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //수정일자
}