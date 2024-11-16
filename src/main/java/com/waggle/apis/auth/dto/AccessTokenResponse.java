package com.waggle.apis.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
