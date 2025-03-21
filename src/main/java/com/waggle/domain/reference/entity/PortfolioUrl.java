package com.waggle.domain.reference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.user.entity.UserPortfolioUrl;
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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "portfolio_url_type")
@Schema(description = "포트폴리오 링크 종류")
public class PortfolioUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "고유키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Column(name = "img_url", length = 1000)
    @Schema(description = "링크 이미지 URL", example = "https://waggle.s3.ap-northeast-2.amazonaws.com/portfolio/github.png")
    @JsonProperty("img_url")
    private String imgUrl;

    @Column(name = "name", nullable = false)
    @Schema(description = "링크명", example = "GitHub")
    @JsonProperty("name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "portfolioUrl", cascade = CascadeType.ALL)
    @Schema(description = "해당 링크를 사용한 사용자 목록")
    @JsonIgnore
    private Set<UserPortfolioUrl> userPortfolioUrls = new HashSet<>();
}
