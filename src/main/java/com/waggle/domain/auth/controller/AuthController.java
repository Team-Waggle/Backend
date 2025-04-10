package com.waggle.domain.auth.controller;

import com.waggle.domain.auth.dto.AccessTokenVo;
import com.waggle.domain.auth.service.AuthService;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.AccessTokenSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/token/exchange")
//    @Operation(
//        summary = "임시 토큰 교환",
//        description = """
//            임시 토큰을 교환하여 액세스 토큰을 발급합니다.
//
//            ⚠️ 로그인 시 Redirect URL의 파라미터 중 token 값이 임시 토큰입니다.\n
//            실제 API 호출 시에는 리다이렉트된 후 token 파라미터가 있으면 자동으로 사용한 후 홈페이지로 리다이렉트되도록 해주세요.\n
//            Swagger UI 테스트 시에만 수동으로 temporary_token 값을 입력해주세요.
//            """,
//        parameters = {
//            @Parameter(
//                name = "temporaryToken",
//                description = "OAuth2 로그인 성공 후 발급된 임시 토큰",
//                required = true,
//                schema = @Schema(type = "string")
//            )
//        },
//        security = @SecurityRequirement(name = "OAuth2")
//    )
//    @ApiResponses({
//        @ApiResponse(responseCode = "201", description = "토큰 교환 성공", content = @Content(schema = @Schema(implementation = AccessTokenSuccessResponse.class))),
//        @ApiResponse(responseCode = "401", description = "유효하지 않은 임시 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    public ResponseEntity<BaseResponse<AccessTokenVo>> exchangeToken(@RequestParam String temporaryToken) {
//        AccessTokenVo accessToken = authService.exchangeTemporaryToken(temporaryToken);
//        return SuccessResponse.of(ApiStatus._CREATE_ACCESS_TOKEN, accessToken);
//    }

    @PostMapping("/tokens/reissue")
    @Operation(
        summary = "액세스 토큰 재발급",
        description = """
            액세스 토큰을 재발급합니다.
            
            ⚠️ 실제 API 호출 시에는 쿠키의 refresh_token이 자동으로 사용됩니다.\n
            Swagger UI 테스트 시에만 쿠키값을 확인하여 수동으로 refresh_token 값을 입력해주세요.
            """,
        security = @SecurityRequirement(name = "OAuth2")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = AccessTokenSuccessResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> reissueAccessToken(
        @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new JwtTokenException(ApiStatus._REFRESH_TOKEN_NOT_FOUND);
        }

        AccessTokenVo accessToken = authService.reissueAccessToken(refreshToken);
        return SuccessResponse.of(ApiStatus._REISSUE_ACCESS_TOKEN, accessToken);
    }

    @PostMapping("/logout")
    @Operation(
        summary = "로그아웃",
        description = """
            서버에서 해당 Refresh Token이 어떤 사용자의 것인지에 대한 기록을 삭제하여 최종적으로 로그아웃합니다.
            
            ⚠️ 로그아웃 후에는 꼭 로컬 스토리지에 저장된 토큰을 삭제하고 새로고침을 해주세요.
            """,
        security = @SecurityRequirement(name = "OAuth2")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "로그아웃 성공", content = @Content()),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> logout(
        @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        authService.deleteRefreshToken(refreshToken);

        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }
}
