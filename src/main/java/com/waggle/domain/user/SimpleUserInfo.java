package com.waggle.domain.user;

import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.user.entity.User;
import java.util.UUID;

public record SimpleUserInfo(
    UUID userId,
    String name,
    String email,
    String profileImageUrl,
    Position position,
    Integer yearCount
) {

    public static SimpleUserInfo from(User user) {
        return new SimpleUserInfo(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getProfileImageUrl(),
            user.getPosition(),
            user.getYearCount()
        );
    }
}
