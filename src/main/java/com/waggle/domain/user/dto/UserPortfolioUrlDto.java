package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserPortfolioUrlDto(

    @JsonProperty("url_type_id")
    @Schema(description = "포트폴리오 링크 종류 고유키", example = "1")
    Long portfolioUrlId,

    @JsonProperty("url")
    @Schema(description = "포트폴리오 링크 URL", example = "www.naver.com")
    String url
) {

}
