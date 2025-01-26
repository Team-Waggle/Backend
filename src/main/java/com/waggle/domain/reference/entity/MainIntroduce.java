package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "introduce_main_type")
@Schema(description = "자기소개 키워드(대분류)")
public class MainIntroduce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "고유키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "키워드명", example = "소통 스타일")
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "mainIntroduce", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Schema(description = "해당 키워드에 속하는 자기소개 소분류 키워드")
    @JsonProperty("sub_introduces")
    @JsonIgnoreProperties("main_introduce")
    @OrderBy("id ASC")
    private Set<SubIntroduce> subIntroduces = new HashSet<>();
}
