package com.waggle.domain.notification.entity;

import com.waggle.domain.notification.NotificationType;
import com.waggle.domain.user.entity.User;
import com.waggle.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(32)")
    private NotificationType type;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Notification(NotificationType type, Long projectId, String redirectUrl, User user) {
        this.type = type;
        this.projectId = projectId;
        this.redirectUrl = redirectUrl;
        this.user = user;
    }
}
