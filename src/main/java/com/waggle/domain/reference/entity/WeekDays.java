package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.user.entity.UserSkill;
import com.waggle.domain.user.entity.UserWeekDays;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "week_days_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WeekDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "short_name", nullable = false)
    @JsonProperty("short_name")
    private String shortName;

    @Column(name = "full_name", nullable = false)
    @JsonProperty("full_name")
    private String fullName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "weekDays", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserWeekDays> userWeekDays = new HashSet<>();
}
