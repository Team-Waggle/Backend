package com.waggle.domain.notification.controller;

import com.waggle.domain.notification.dto.NotificationResponse;
import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.notification.service.NotificationService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.CursorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.EmptySuccessResponse;
import com.waggle.global.response.swagger.NotificationsSuccessResponse;
import com.waggle.global.response.swagger.UnreadCountSuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인앱 알림", description = "인앱 알림 관련 API")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(
        summary = "알림 목록 조회",
        description = "현재 로그인된 사용자의 알림 목록을 조회합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "알림 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = NotificationsSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<CursorResponse<NotificationResponse>>> getMyNotifications(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "5") int size
    ) {
        List<Notification> notifications = notificationService.getNotifications(
            userPrincipal.getUser(), cursor, size
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            CursorResponse.of(
                notifications,
                size,
                NotificationResponse::from,
                Notification::getId
            )
        );
    }

    @GetMapping("/unread-count")
    @Operation(
        summary = "읽지 않은 알림 개수 조회",
        description = "현재 로그인된 사용자의 읽지 않은 알림 개수를 조회합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "읽지 않은 알림 개수 조회 성공",
            content = @Content(
                schema = @Schema(implementation = UnreadCountSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<Integer>> getUnreadNotificationCount(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            notificationService.getUnreadNotificationCount(userPrincipal.getUser())
        );
    }

    @PatchMapping("/read-all")
    @Operation(
        summary = "모든 알림 읽음 처리",
        description = "현재 로그인된 사용자의 모든 알림을 읽음 상태로 변경합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "모든 알림 읽음 처리 성공",
            content = @Content(
                schema = @Schema(implementation = EmptySuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<Void>> readAllNotifications(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        notificationService.readAllNotifications(userPrincipal.getUser());
        return SuccessResponse.of(ApiStatus._OK, null);
    }
}
