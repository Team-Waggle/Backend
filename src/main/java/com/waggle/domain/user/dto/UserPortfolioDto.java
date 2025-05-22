package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.PortfolioType;
import com.waggle.domain.user.entity.UserPortfolio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record UserPortfolioDto(
    @NotNull(message = "포트폴리오 종류는 필수 항목입니다")
    @JsonProperty("portfolio_type")
    @Schema(description = "포트폴리오 링크 종류", example = "GITHUB")
    PortfolioType portfolioType,

    @URL(message = "유효한 URL 형식이 아닙니다")
    @JsonProperty("url")
    @Schema(description = "포트폴리오 링크 URL", example = "github.com/username")
    String url
) {

    public static UserPortfolioDto from(UserPortfolio userPortfolio) {
        return new UserPortfolioDto(userPortfolio.getPortfolioType(), userPortfolio.getUrl());
    }
}
