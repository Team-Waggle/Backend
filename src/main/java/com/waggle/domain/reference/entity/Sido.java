package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "sido_type")
@Schema(description = "지역(시/도)")
public class Sido {

    @Id
    @Column(name = "id", length = 2, nullable = false)
    @Schema(description = "고유키", example = "11")
    @JsonProperty("id")
    private String id;

    @Column(name = "name", nullable = false)
    @Schema(description = "시/도명", example = "서울")
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "preferSido")
    @Schema(description = "해당 시/도를 선택한 사용자 목록")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
