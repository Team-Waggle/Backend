package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.user.entity.UserSkill;
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
@Table(name = "skill_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "img_url", length = 1000, nullable = false)
    @JsonProperty("img_url")
    private String imgUrl;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserSkill> userSkills = new HashSet<>();
}
