package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateUserDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("jobs")
    private List<UserJobDto> jobs;

    @JsonProperty("industries")
    private List<Long> industries;

    @JsonProperty("skills")
    private List<Long> skills;

    @JsonProperty("prefer_week_days")
    private List<Long> preferWeekDays;

    @JsonProperty("prefer_tow_id")
    private Long preferTowId;

    @JsonProperty("prefer_wow_id")
    private Long preferWowId;

    @JsonProperty("prefer_sido_id")
    private String preferSidoId;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("portfolio_urls")
    private List<UserPortfolioUrlDto> portfolioUrls;
}
