package com.waggle.domain.notification.service;

import com.waggle.domain.notification.NotificationType;
import com.waggle.domain.notification.dto.NotificationRequestDto;
import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.notification.repository.NotificationRepository;
import com.waggle.domain.user.entity.User;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public Notification createNotification(
        NotificationRequestDto notificationRequestDto,
        User recipient
    ) {
        NotificationType type = notificationRequestDto.type();
        Notification notification = Notification.builder()
            .title(type.getTitle())
            .content(type.getContent(notificationRequestDto.contentArgs()))
            .redirectUrl(notificationRequestDto.redirectUrl())
            .user(recipient)
            .build();
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotifications(User user, Long cursor, int size) {
        Instant cursorCreatedAt = null;

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

    @Override
    @Transactional(readOnly = true)
    public int getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserIdAndIsReadFalse(user.getId());
    }

    @Override
    @Transactional
    public void readAllNotifications(User user) {
        notificationRepository.updateIsReadByUserIdAndIsReadFalse(user.getId(), true);
    }
}
