package com.waggle.domain.follow.controller;

import com.waggle.domain.follow.dto.ToggleFollowRequest;
import com.waggle.domain.follow.service.FollowService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/follows")
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> toggleFollow(
        @RequestBody ToggleFollowRequest toggleFollowRequest,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            this.followService.toggleFollow(toggleFollowRequest, userPrincipal.getUser())
        );
    }
}
