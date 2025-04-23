package com.waggle.domain.notification.controller;

import com.waggle.domain.notification.dto.NotificationResponseDto;
import com.waggle.domain.notification.service.NotificationService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인앱 알림", description = "인앱 알림 관련 API")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<BaseResponse<Set<NotificationResponseDto>>> getMyNotifications(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            notificationService.getNotifications(userDetails.getUser()).stream()
                .map(NotificationResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<BaseResponse<Integer>> getUnreadNotificationCount(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            notificationService.getUnreadNotificationCount(userDetails.getUser())
        );
    }

    @PatchMapping("/read-all")
    public ResponseEntity<BaseResponse<Void>> readAllNotifications(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        notificationService.readAllNotifications(userDetails.getUser());
        return SuccessResponse.of(ApiStatus._OK, null);
    }
}
