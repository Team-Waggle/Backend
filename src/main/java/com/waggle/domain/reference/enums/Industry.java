package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Industry {
    FINANCE("금융"),
    REAL_ESTATE("부동산"),
    INTERIOR("인테리어"),
    MEDICAL_HEALTHCARE("의료/헬스케어"),
    ECOMMERCE("이커머스"),
    ENTERTAINMENT("엔터테인먼트"),
    TRAVEL("여행"),
    SOCIAL_NETWORK("소셜네트워크"),
    CULTURE_ART("문화/예술"),
    BEAUTY_FASHION("뷰티/패션"),
    RELIGION("종교"),
    SALES_DISTRIBUTION("판매/유통"),
    EDUCATION("교육"),
    CONSTRUCTION("건설"),
    HEALTH("건강"),
    PARENTING("육아/출산"),
    MEDIA_ADVERTISING("미디어/광고"),
    ETC("기타");

    @JsonProperty("display_name")
    private final String displayName;
}
