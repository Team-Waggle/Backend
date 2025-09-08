package com.waggle.domain.project.entity;

import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.WorkPeriod;
import com.waggle.domain.reference.enums.WorkWay;
import com.waggle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String title; //제목

    private Industry industry; //산업 분야

    private WorkWay workWay; //진행 방식

    @Column(name = "recruitment_end_date", nullable = false)
    private LocalDate recruitmentEndDate; //프로젝트 모집 마감 일자

    @Enumerated(EnumType.STRING)
    @Column(name = "work_period")
    private WorkPeriod workPeriod; //진행 기간

    //cascade=강제삭제방식
    //mappedBy=연결된 필드 변수 명
    //set=배열(gpt 추천)
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private Set<ProjectRecruitmentJob> recruitmentJobs; //모집 직무
//
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private Set<ProjectMemberJob> memberJobs; //멤버 직무
//
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private Set<ProjectSkill> projectSkills; //사용 스킬

    @Column(length = 1000)
    @Schema(description = "소개", example = "기본적으로 Spring을 쓰실 줄 알며, RestAPI를 잘 쓰시는 분을 모집합니다.")
    private String detail; //소개

    @Column(name = "contact_url")
    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    private String contactUrl; //연락 링크

    @Column(name = "reference_url")
    @Schema(description = "참조 링크", example = "www.naver.com")
    private String referenceUrl; //참고 링크

    @Column(name = "bookmark_count")
    @Schema(description = "북마크 수(스크랩)", example = "0")
    private int bookmarkCount; //북마크 수(스크랩)

//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private Set<ProjectMember> projectMembers; //참여자
//
//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private Set<ProjectApplicant> projectApplicants; //지원자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; //생성일자

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //수정일자

    public void update(
        String title,
        Industry industry,
        WorkWay workWay,
        LocalDate recruitmentEndDate,
        WorkPeriod workPeriod,
        String detail,
        String contactUrl,
        String referenceUrl
    ) {
        this.title = title;
        this.industry = industry;
        this.workWay = workWay;
        this.recruitmentEndDate = recruitmentEndDate;
        this.workPeriod = workPeriod;
        this.detail = detail;
        this.contactUrl = contactUrl;
        this.referenceUrl = referenceUrl;
    }

    public void incrementBookmarkCount() {
        bookmarkCount++;
    }

    public void decrementBookmarkCount() {
        bookmarkCount--;
    }
}
