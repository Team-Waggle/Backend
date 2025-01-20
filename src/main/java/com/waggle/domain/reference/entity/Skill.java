package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.ProjectSkill;
import com.waggle.domain.user.entity.UserSkill;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "skill_type")
@Schema(description = "스킬")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "img_url", length = 1000, nullable = false)
    @Schema(description = "스킬 이미지 URL", example = "https://waggle.s3.ap-northeast-2.amazonaws.com/skill/1.png")
    @JsonProperty("img_url")
    private String imgUrl;

    @Column(name = "name", nullable = false)
    @Schema(description = "스킬명", example = "Java")
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    @Schema(description = "해당 스킬을 보유한 사용자 목록")
    @JsonIgnore
    private Set<UserSkill> userSkills = new HashSet<>();

    //new HashSet<>() = 빈 배열
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL) //skill이 여러개 올 수 있고, 중간테이블은 project를 1개씩밖에 못가져서
    @JsonIgnore //manyTomany로 연결되었을 때 무한 반복되는 것을 끊어내기 위해 사용
    private Set<ProjectSkill> projectSkills = new HashSet<>();
}
