package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.ProjectJob;
import com.waggle.domain.user.entity.UserJob;
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
@Table(name = "job_type")
@Schema(description = "직무")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "고유키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "직무명", example = "프론트엔드")
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @Schema(description = "해당 직무를 선택한 사용자 목록")
    @JsonIgnore
    private Set<UserJob> userJobs = new HashSet<>();

    //new HashSet<>() = 빈 배열
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL) //job이 여러개 올 수 있고, 중간테이블은 project를 1개씩밖에 못가져서
    @JsonIgnore //manyTomany로 연결되었을 때 무한 반복되는 것을 끊어내기 위해 사용
    private Set<ProjectJob> projectJobs = new HashSet<>();
}
