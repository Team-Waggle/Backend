package com.waggle.domain.user.entity;

import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.project.entity.ProjectBookmark;
import com.waggle.domain.project.entity.ProjectMember;
import com.waggle.domain.reference.enums.Sido;
import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.domain.reference.enums.WorkWay;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "profile_img_url")
    private String profileImageUrl;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserJob> userJobs = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserIndustry> userIndustries = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserSkill> userSkills = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserWeekDays> userWeekDays = new HashSet<>();

    @Column(name = "prefer_tow_id")
    private WorkTime preferTow;

    @Column(name = "prefer_wow_id")
    private WorkWay preferWow;

    @Column(name = "prefer_sido_id")
    private Sido preferSido;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserIntroduce> userIntroduces = new HashSet<>();

    @Column(name = "detail")
    private String detail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserPortfolioUrl> userPortfolioUrls = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProjectMember> projectMembers = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProjectApplicant> projectApplicants = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProjectBookmark> projectBookmarks = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static class UserBuilder {

        private Set<UserJob> userJobs = new HashSet<>();
        private Set<UserIndustry> userIndustries = new HashSet<>();
        private Set<UserSkill> userSkills = new HashSet<>();
        private Set<UserWeekDays> userWeekDays = new HashSet<>();
        private Set<UserIntroduce> userIntroduces = new HashSet<>();
        private Set<UserPortfolioUrl> userPortfolioUrls = new HashSet<>();
        private Set<ProjectMember> projectMembers = new HashSet<>();
        private Set<ProjectApplicant> projectApplicants = new HashSet<>();
        private Set<ProjectBookmark> projectBookmarks = new HashSet<>();
    }
}
