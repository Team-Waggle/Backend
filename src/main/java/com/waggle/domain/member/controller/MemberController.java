package com.waggle.domain.member.controller;

import com.waggle.domain.member.Member;
import com.waggle.domain.member.dto.MemberResponse;
import com.waggle.domain.member.dto.UpdatePositionRequest;
import com.waggle.domain.member.service.MemberService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.EmptySuccessResponse;
import com.waggle.global.response.swagger.MemberSuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "멤버 관리 API")
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(
        summary = "멤버 직무 변경",
        description = "프로젝트 멤버의 직무를 변경합니다. 프로젝트 리더만 변경할 수 있습니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "멤버 포지션 변경 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemberSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "포지션 변경 권한 없음 (프로젝트 리더가 아님)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 멤버",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PatchMapping("/{memberId}")
    public ResponseEntity<BaseResponse<MemberResponse>> updateMemberPosition(
        @PathVariable Long memberId,
        @RequestBody UpdatePositionRequest updatePositionRequest,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Member member = memberService.updateMemberPosition(
            memberId,
            updatePositionRequest,
            userPrincipal.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            MemberResponse.from(member)
        );
    }

    @Operation(
        summary = "멤버 제거",
        description = "프로젝트에서 멤버를 제거합니다. 프로젝트 리더는 다른 멤버를 추방할 수 있고, 멤버 본인은 자신을 탈퇴시킬 수 있습니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "멤버 제거 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmptySuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "멤버 제거 권한 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 멤버",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<BaseResponse<Void>> deleteMember(
        @PathVariable Long memberId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        memberService.deleteMember(memberId, userPrincipal.getUser());

        return SuccessResponse.of(
            ApiStatus._NO_CONTENT,
            null
        );
    }
}
