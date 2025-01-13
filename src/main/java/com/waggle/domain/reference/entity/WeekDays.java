package com.waggle.domain.reference.entity;

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
    private Long id;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "weekDays", cascade = CascadeType.ALL)
    private Set<UserWeekDays> userWeekDays = new HashSet<>();
}
