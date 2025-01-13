package com.waggle.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.entity.Sido;
import com.waggle.domain.reference.entity.TimeOfWorking;
import com.waggle.domain.reference.entity.WaysOfWorking;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @JsonProperty("id")
    private UUID id;

    @Column(name = "provider", nullable = false)
    @JsonProperty("provider")
    private String provider;

    @Column(name = "provider_id", nullable = false)
    @JsonProperty("provider_id")
    private String providerId;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "email", nullable = false)
    @JsonProperty("email")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("jobs")
    private Set<UserJob> userJobs = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("industries")
    private Set<UserIndustry> userIndustries = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("skills")
    private Set<UserSkill> userSkills = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("week_days")
    private Set<UserWeekDays> userWeekDays = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "prefer_tow_id")
    @JsonProperty("prefer_tow")
    private TimeOfWorking preferTow;

    @ManyToOne
    @JoinColumn(name = "prefer_wow_id")
    @JsonProperty("prefer_wow")
    private WaysOfWorking preferWow;

    @ManyToOne
    @JoinColumn(name = "prefer_sido_id")
    @JsonProperty("prefer_sido")
    private Sido preferSido;

    @Column(name = "detail")
    @JsonProperty("detail")
    private String detail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonProperty("portfolio_urls")
    private Set<UserPortfolioUrl> userPortfolioUrls = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public void clearInfo() {
        this.userJobs.clear();
        this.userIndustries.clear();
        this.userSkills.clear();
        this.userWeekDays.clear();
        this.userPortfolioUrls.clear();
    }
}
