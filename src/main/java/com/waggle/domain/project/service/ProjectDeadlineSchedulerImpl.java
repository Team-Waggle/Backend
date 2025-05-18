package com.waggle.domain.project.service;

import com.waggle.domain.notification.NotificationType;
import com.waggle.domain.notification.dto.NotificationRequestDto;
import com.waggle.domain.notification.service.NotificationService;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.entity.ProjectBookmark;
import com.waggle.domain.project.repository.ProjectBookmarkRepository;
import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.user.entity.User;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectDeadlineSchedulerImpl implements ProjectDeadlineScheduler {

    private final ProjectRepository projectRepository;
    private final ProjectBookmarkRepository projectBookmarkRepository;
    private final NotificationService notificationService;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void createProjectDeadlineAlerts() {
        LocalDate today = LocalDate.now();

        // 마감일이 오늘인 프로젝트 처리
        sendDeadlineNotifications(today, NotificationType.DEADLINE_CLOSED);

        // 마감일이 내일인 프로젝트 처리
        sendDeadlineNotifications(today.plusDays(1), NotificationType.DEADLINE_APPROACHING);

        // 마감일이 2일 후인 프로젝트 처리
        sendDeadlineNotifications(today.plusDays(2), NotificationType.DEADLINE_APPROACHING);

        // 마감일이 3일 후인 프로젝트 처리
        sendDeadlineNotifications(today.plusDays(3), NotificationType.DEADLINE_APPROACHING);
    }

    private void sendDeadlineNotifications(LocalDate date, NotificationType type) {
        LocalDate today = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(date, today);

        projectRepository.findByRecruitmentEndDate(date)
            .forEach(project -> {
                switch (type) {
                    case DEADLINE_CLOSED -> sendNotificationsToBookmarkedUsers(project, type);
                    case DEADLINE_APPROACHING ->
                        sendNotificationsToBookmarkedUsers(project, type, daysRemaining);
                }
            });
    }

    private void sendNotificationsToBookmarkedUsers(
        Project project,
        NotificationType type,
        Object... args
    ) {
        List<ProjectBookmark> bookmarks = projectBookmarkRepository.findByProjectId(
            project.getId()
        );

        for (ProjectBookmark bookmark : bookmarks) {
            User user = bookmark.getUser();
            String redirectUrl = "/projects/" + project.getId();

            Object[] contentArgs = Stream.concat(
                Stream.of(project.getTitle()),
                Arrays.stream(args)
            ).toArray();

            NotificationRequestDto notificationRequestDto = NotificationRequestDto.of(
                type,
                redirectUrl,
                contentArgs
            );

            notificationService.createNotification(notificationRequestDto, user);
        }
    }
}
