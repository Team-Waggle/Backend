package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.PortfolioType;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserPortfolioDto(

    @JsonProperty("portfolio_type")
    @Schema(description = "포트폴리오 링크 종류", example = "GITHUB")
    PortfolioType portfolioType,

    @JsonProperty("url")
    @Schema(description = "포트폴리오 링크 URL", example = "github.com/username")
    String url
) {

}
