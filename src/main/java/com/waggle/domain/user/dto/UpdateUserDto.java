package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Set;

@Getter
public class UpdateUserDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("jobs")
    private Set<UserJobDto> jobs;

    @JsonProperty("industries")
    private Set<Long> industries;

    @JsonProperty("skills")
    private Set<Long> skills;

    @JsonProperty("prefer_week_days")
    private Set<Long> preferWeekDays;

    @JsonProperty("prefer_tow_id")
    private Long preferTowId;

    @JsonProperty("prefer_wow_id")
    private Long preferWowId;

    @JsonProperty("prefer_sido_id")
    private String preferSidoId;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("portfolio_urls")
    private Set<UserPortfolioUrlDto> portfolioUrls;
}
