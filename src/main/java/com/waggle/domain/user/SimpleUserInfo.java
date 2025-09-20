package com.waggle.domain.user;

import com.waggle.domain.user.dto.UserPositionDto;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.entity.UserPosition;
import java.util.List;
import java.util.UUID;

public record SimpleUserInfo(
    UUID userId,
    String name,
    String email,
    String profileImageUrl,
    List<UserPositionDto> userPositions
) {

    public static SimpleUserInfo of(User user, List<UserPosition> userPositions) {
        return new SimpleUserInfo(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getProfileImageUrl(),
            userPositions.stream().map(UserPositionDto::from).toList()
        );
    }
}
