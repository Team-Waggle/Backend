package com.waggle.domain.notification.controller;

import com.waggle.domain.ApiV1Controller;
import com.waggle.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인앱 알림", description = "인앱 알림 관련 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController extends ApiV1Controller {

    private final NotificationService notificationService;


}
