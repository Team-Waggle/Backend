package com.waggle.domain.follow.dto;

public record FollowResponse(
    int followedCount,
    int followingCount
) {

    public static FollowResponse of(int followedCount, int followingCount) {
        return new FollowResponse(followedCount, followingCount);
    }
}
