package com.waggle.domain.notification.service;

import com.waggle.domain.notification.NotificationType;
import com.waggle.domain.notification.dto.NotificationRequestDto;
import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.notification.repository.NotificationRepository;
import com.waggle.domain.user.entity.User;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
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
            .content(type.getContent(notificationRequestDto.type().getContent()))
            .redirectUrl(notificationRequestDto.redirectUrl())
            .user(recipient)
            .build();
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Notification> getNotifications(User user) {
        return new HashSet<>(notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
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
