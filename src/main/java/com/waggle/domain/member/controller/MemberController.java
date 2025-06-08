package com.waggle.domain.member.controller;

import com.waggle.domain.member.Member;
import com.waggle.domain.member.dto.MemberResponse;
import com.waggle.domain.member.dto.UpdatePositionDto;
import com.waggle.domain.member.service.MemberService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/{memberId}")
    public ResponseEntity<BaseResponse<MemberResponse>> updateMemberPosition(
        @PathVariable Long memberId,
        @RequestBody UpdatePositionDto updatePositionDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member = memberService.updateMemberPosition(
            memberId,
            updatePositionDto,
            userDetails.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            MemberResponse.from(member)
        );
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<BaseResponse<Void>> deleteMember(
        @PathVariable Long memberId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        memberService.deleteMember(memberId, userDetails.getUser());

        return SuccessResponse.of(
            ApiStatus._NO_CONTENT,
            null
        );
    }
}
