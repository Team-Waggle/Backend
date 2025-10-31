package com.waggle.domain.user.entity;

import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.reference.enums.Sido;
import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.domain.reference.enums.WorkWay;
import com.waggle.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users",
    indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_email", columnList = "email"),
    }
)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_img_url")
    private String profileImageUrl;

    @Column(name = "preferred_work_time")
    private WorkTime workTime;

    @Column(name = "preferred_work_way")
    private WorkWay workWay;

    @Column(name = "preferred_sido")
    private Sido sido;

    @Column(name = "detail")
    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @Column(name = "year_count")
    private Integer yearCount;

    @Builder
    public User(
        String provider,
        String providerId,
        String name,
        String email,
        String profileImageUrl,
        WorkTime workTime,
        WorkWay workWay,
        Sido sido,
        String detail,
        Position position,
        Integer yearCount
    ) {
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.workTime = workTime;
        this.workWay = workWay;
        this.sido = sido;
        this.detail = detail;
        this.position = position;
        this.yearCount = yearCount;
    }

    public void update(
        String name,
        WorkTime workTime,
        WorkWay workWay,
        Sido sido,
        String detail,
        Position position,
        Integer yearCount
    ) {
        this.name = name;
        this.workTime = workTime;
        this.workWay = workWay;
        this.sido = sido;
        this.detail = detail;
        this.position = position;
        this.yearCount = yearCount;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
