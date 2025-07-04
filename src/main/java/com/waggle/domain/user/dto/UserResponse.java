package com.waggle.domain.user.dto;

import com.waggle.domain.reference.enums.Sido;
import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.domain.reference.enums.WorkWay;
import com.waggle.domain.user.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    String profileImageUrl,
    WorkTime workTime,
    WorkWay workWay,
    Sido sido,
    String detail,
    Instant createdAt,
    Instant updatedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getProfileImageUrl(),
            user.getWorkTime(),
            user.getWorkWay(),
            user.getSido(),
            user.getDetail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
