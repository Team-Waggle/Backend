package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.ProjectSkill;
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

    //new HashSet<>() = 빈 배열
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL) //skill이 여러개 올 수 있고, 중간테이블은 project를 1개씩밖에 못가져서
    private Set<ProjectSkill> projectSkills = new HashSet<>();
}
