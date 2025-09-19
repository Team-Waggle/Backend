package com.waggle.domain.notification.service;

import com.waggle.domain.notification.NotificationType;
import com.waggle.domain.notification.dto.CreateNotificationRequest;
import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.notification.repository.NotificationRepository;
import com.waggle.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification createNotification(
        CreateNotificationRequest createNotificationRequest,
        User recipient
    ) {
        NotificationType type = createNotificationRequest.type();
        Notification notification = Notification.builder()
            .title(type.getTitle())
            .content(type.getContent(createNotificationRequest.contentArgs()))
            .redirectUrl(createNotificationRequest.redirectUrl())
            .user(recipient)
            .build();
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotifications(User user, Long cursor, int size) {
        LocalDateTime cursorCreatedAt = null;

        if (cursor != null) {
            cursorCreatedAt = notificationRepository.findCreatedAtById(cursor).orElse(null);
        }

        Pageable pageable = PageRequest.of(0, size + 1);

        return notificationRepository.findByUserIdWithCursor(
            user.getId(),
            cursor,
            cursorCreatedAt,
            pageable
        );
    }

    @Transactional(readOnly = true)
    public int getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserIdAndIsReadFalse(user.getId());
    }

    @Transactional
    public void readAllNotifications(User user) {
        notificationRepository.updateIsReadByUserIdAndIsReadFalse(user.getId(), true);
    }
}
