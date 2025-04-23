package com.waggle.domain.notification.service;

import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.user.entity.User;
import java.util.Set;

public interface NotificationService {

    Set<Notification> getNotifications(User user);

    int getUnreadNotificationCount(User user);

    void readAllNotifications(User user);
}
