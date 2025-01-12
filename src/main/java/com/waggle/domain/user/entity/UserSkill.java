package com.waggle.domain.user.entity;

import com.waggle.domain.reference.entity.Skill;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
