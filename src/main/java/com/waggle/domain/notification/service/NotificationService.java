package com.waggle.domain.notification.service;

import com.waggle.domain.notification.dto.NotificationRequestDto;
import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.user.entity.User;
import java.util.List;

public interface NotificationService {

    Notification createNotification(NotificationRequestDto notificationRequestDto, User recipient);

    List<Notification> getNotifications(User user);

    int getUnreadNotificationCount(User user);

    void readAllNotifications(User user);
}
