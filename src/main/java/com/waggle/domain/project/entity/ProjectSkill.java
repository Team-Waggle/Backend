package com.waggle.domain.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waggle.domain.reference.entity.Skill;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
public class ProjectSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    @JsonIgnore
    private Project project;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
