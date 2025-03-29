package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "week_days_type")
@Schema(description = "요일")
public class WeekDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "고유키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Column(name = "short_name", nullable = false)
    @Schema(description = "짧은 요일명", example = "월")
    @JsonProperty("short_name")
    private String shortName;

    @Column(name = "full_name", nullable = false)
    @Schema(description = "긴 요일명", example = "월요일")
    @JsonProperty("full_name")
    private String fullName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "weekDays", cascade = CascadeType.ALL)
//    @Schema(description = "해당 요일을 선호하는 사용자 목록")
//    @JsonIgnore
//    private Set<UserWeekDays> userWeekDays = new HashSet<>();
}
