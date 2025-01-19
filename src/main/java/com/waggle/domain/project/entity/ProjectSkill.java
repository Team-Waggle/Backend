package com.waggle.domain.project.entity;

import com.waggle.domain.reference.entity.Skill;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ProjectSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
