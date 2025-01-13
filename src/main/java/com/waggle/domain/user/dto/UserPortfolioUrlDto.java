package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPortfolioUrlDto {

    @JsonProperty("url_type_id")
    private Long portfolioUrlId;

    @JsonProperty("url")
    private String url;
}
