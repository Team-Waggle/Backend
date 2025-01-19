package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.user.entity.UserIndustry;
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
@Table(name = "industrial_type")
@Schema(description = "산업 분야")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "고유키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "산업 분야명", example = "금융")
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL)
    @Schema(description = "해당 산업 분야를 선택한 사용자 목록")
    @JsonIgnore
    private Set<UserIndustry> userIndustries = new HashSet<>();
}
