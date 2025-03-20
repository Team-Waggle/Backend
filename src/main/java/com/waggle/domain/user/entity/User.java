package com.waggle.domain.user.entity;

import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.project.entity.ProjectBookmark;
import com.waggle.domain.project.entity.ProjectMember;
import com.waggle.domain.reference.entity.Sido;
import com.waggle.domain.reference.entity.TimeOfWorking;
import com.waggle.domain.reference.entity.WaysOfWorking;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

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

    @ManyToOne
    @JoinColumn(name = "prefer_tow_id")
    private TimeOfWorking preferTow;

    @ManyToOne
    @JoinColumn(name = "prefer_wow_id")
    private WaysOfWorking preferWow;

    @ManyToOne
    @JoinColumn(name = "prefer_sido_id")
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

    public void clearInfo() {
        this.userJobs.clear();
        this.userIndustries.clear();
        this.userSkills.clear();
        this.userWeekDays.clear();
        this.userIndustries.clear();
        this.userPortfolioUrls.clear();
    }
}
