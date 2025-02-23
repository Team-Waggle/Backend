package com.waggle.domain.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonProperty("project")
    @JsonIgnoreProperties("users")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty("user")
    @JsonIgnoreProperties("projects")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "is_leader", nullable = false, columnDefinition = "boolean default false")
    @Schema(description = "프로젝트 리더 여부", example = "true")
    @JsonProperty("is_leader")
    private boolean isLeader;

    @CreationTimestamp
    @Schema(description = "참가 일자", example = "2021-08-01T00:00:00")
    @JsonProperty("joined_at")
    private LocalDateTime joinedAt;
}
