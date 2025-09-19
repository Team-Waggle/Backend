package com.waggle.domain.application;

import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.user.entity.User;
import com.waggle.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, updatable = false)
    private ProjectV2 project;

    private LocalDateTime deletedAt;

    @Builder
    private Application(Position position, User user, ProjectV2 project) {
        this.position = position;
        this.status = ApplicationStatus.PENDING;
        this.user = user;
        this.project = project;
    }

    public void approve() {
        if (status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only pending application can be approved");
        }
        this.status = ApplicationStatus.APPROVED;
    }

    public void reject() {
        if (status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only pending application can be rejected");
        }
        this.status = ApplicationStatus.REJECTED;
    }

    public void cancel() {
        if (status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only pending application can be cancelled");
        }
        this.status = ApplicationStatus.CANCELLED;
        this.deletedAt = LocalDateTime.now();
    }
}
