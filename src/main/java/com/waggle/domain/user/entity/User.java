package com.waggle.domain.user.entity;

import com.fasterxml.jackson.annotation.*;
import com.waggle.domain.auth.entity.RefreshToken;
import com.waggle.domain.project.entity.ProjectUser;
import com.waggle.domain.reference.entity.Sido;
import com.waggle.domain.reference.entity.TimeOfWorking;
import com.waggle.domain.reference.entity.WaysOfWorking;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Schema(description = "사용자 정보")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    private UUID id;

    @Column(name = "provider", nullable = false)
    @Schema(description = "OAuth 제공자", example = "google")
    @JsonProperty("provider")
    private String provider;

    @Column(name = "provider_id", nullable = false)
    @Schema(description = "OAuth ID", example = "1234567890")
    @JsonProperty("provider_id")
    private String providerId;

    @Column(name = "profile_img_url")
    @Schema(description = "프로필 이미지 URL")
    @JsonProperty("profile_img_url")
    private String profileImageUrl;

    @Column(name = "name", nullable = false)
    @Schema(description = "사용자 이름", example = "홍길동")
    @JsonProperty("name")
    private String name;

    @Column(name = "email", nullable = false)
    @Schema(description = "사용자 이메일", example = "hong@gmail.com")
    @JsonProperty("email")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "사용자 직무 정보")
    @JsonProperty("jobs")
    private Set<UserJob> userJobs = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "사용자 관심 산업 정보")
    @JsonProperty("industries")
    private Set<UserIndustry> userIndustries = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "사용자 보유 기술 정보")
    @JsonProperty("skills")
    private Set<UserSkill> userSkills = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "사용자 선호 요일 정보")
    @JsonProperty("week_days")
    private Set<UserWeekDays> userWeekDays = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "prefer_tow_id")
    @Schema(description = "사용자 선호 작업 시간 정보")
    @JsonProperty("prefer_tow")
    private TimeOfWorking preferTow;

    @ManyToOne
    @JoinColumn(name = "prefer_wow_id")
    @Schema(description = "사용자 선호 작업 방식 정보")
    @JsonProperty("prefer_wow")
    private WaysOfWorking preferWow;

    @ManyToOne
    @JoinColumn(name = "prefer_sido_id")
    @Schema(description = "사용자 지역 정보")
    @JsonProperty("prefer_sido")
    private Sido preferSido;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "사용자 소개 키워드 정보")
    @JsonProperty("introduces")
    private Set<UserIntroduce> userIntroduces = new HashSet<>();

    @Column(name = "detail")
    @Schema(description = "사용자 자기소개")
    @JsonProperty("detail")
    private String detail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "사용자 포트폴리오 링크 정보")
    @JsonProperty("portfolio_urls")
    private Set<UserPortfolioUrl> userPortfolioUrls = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "사용자가 참여한 프로젝트 정보")
    @JsonProperty("projects")
    private Set<ProjectUser> projectUsers = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "수정일자", example = "2021-07-01T00:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @JsonIgnore
    private RefreshToken refreshToken;

    public void clearInfo() {
        this.userJobs.clear();
        this.userIndustries.clear();
        this.userSkills.clear();
        this.userWeekDays.clear();
        this.userIndustries.clear();
        this.userPortfolioUrls.clear();
    }
}
