package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.entity.User;
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
@Table(name = "wow_type")
@Schema(description = "진행 방식")
public class WaysOfWorking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "고유키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "진행 방식명", example = "온라인")
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "preferWow", fetch = FetchType.LAZY)
    @Schema(description = "해당 진행 방식을 선호하는 사용자 목록")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "waysOfWorking", cascade = CascadeType.ALL)
    @Schema(description = "해당 진행 방식을 선택한 프로젝트 목록")
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();
}
