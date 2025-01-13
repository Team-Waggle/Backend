package com.waggle.domain.user.entity;

import com.waggle.domain.reference.entity.Skill;
import com.waggle.domain.reference.entity.WeekDays;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserWeekDays {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "week_days_id")
    private WeekDays weekDays;
}
