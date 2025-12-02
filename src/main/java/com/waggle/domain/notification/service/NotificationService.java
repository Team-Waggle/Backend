package com.waggle.domain.notification.service;

import com.waggle.domain.notification.dto.CreateNotificationRequest;
import com.waggle.domain.notification.dto.NotificationResponse;
import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.notification.repository.NotificationRepository;
import com.waggle.domain.project.SimpleProjectInfo;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Notification createNotification(
        CreateNotificationRequest createNotificationRequest,
        User recipient
    ) {
        Notification notification = Notification.builder()
            .type(createNotificationRequest.type())
            .projectId(createNotificationRequest.projectId())
            .redirectUrl(createNotificationRequest.redirectUrl())
            .user(recipient)
            .build();
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(User user, Long cursor, int size) {
        LocalDateTime cursorCreatedAt = null;

        if (cursor != null) {
            cursorCreatedAt = notificationRepository.findCreatedAtById(cursor).orElse(null);
        }

        Pageable pageable = PageRequest.of(0, size + 1);

        List<Notification> notifications = notificationRepository.findByUserIdWithCursor(
            user.getId(),
            cursor,
            cursorCreatedAt,
            pageable
        );

        // Extract projectIds and fetch projects
        List<Long> projectIds = notifications.stream()
            .map(Notification::getProjectId)
            .filter(id -> id != null)
            .distinct()
            .toList();

        Map<Long, Project> projectMap = projectRepository.findAllById(projectIds).stream()
            .collect(Collectors.toMap(Project::getId, project -> project));

        // Convert to NotificationResponse with project info
        return notifications.stream()
            .map(notification -> {
                SimpleProjectInfo projectInfo = null;
                if (notification.getProjectId() != null) {
                    Project project = projectMap.get(notification.getProjectId());
                    if (project != null) {
                        projectInfo = SimpleProjectInfo.from(project);
                    }
                }
                return NotificationResponse.of(notification, projectInfo);
            })
            .toList();
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
