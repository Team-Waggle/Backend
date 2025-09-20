package com.waggle.domain.follow.service;

import com.waggle.domain.follow.Follow;
import com.waggle.domain.follow.dto.ToggleFollowRequest;
import com.waggle.domain.follow.repository.FollowRepository;
import com.waggle.domain.user.SimpleUserInfo;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.entity.UserPosition;
import com.waggle.domain.user.repository.UserPositionRepository;
import com.waggle.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Boolean toggleFollow(ToggleFollowRequest toggleFollowRequest, User user) {
        UUID followeeId = toggleFollowRequest.userId();
        UUID followerId = user.getId();

        if (followeeId.equals(followerId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        User followee = userRepository.findById(followeeId).orElseThrow(
            () -> new EntityNotFoundException("User not found with id: " + followeeId));

        if (followRepository.existsByFollowerAndFollowee(user, followee)) {
            followRepository.deleteByFollowerAndFollowee(user, followee);
            return false;
        }

        Follow follow = Follow.builder()
            .follower(user)
            .followee(followee)
            .build();

        followRepository.save(follow);

        return true;
    }

    @Transactional(readOnly = true)
    public List<SimpleUserInfo> getUserFollowees(User user) {
        List<Follow> follows = followRepository.findFolloweesWithUser(user);
        List<UUID> userIds = follows.stream()
            .map(follow -> follow.getFollowee().getId())
            .toList();

        List<UserPosition> userPositions = userPositionRepository.findByUserIdIn(userIds);

        Map<UUID, List<UserPosition>> positionsByUserId = userPositions.stream()
            .collect(Collectors.groupingBy(
                userPosition -> userPosition.getUser().getId(),
                LinkedHashMap::new,
                Collectors.toList()
            ));

        return follows.stream()
            .map(follow -> {
                User followee = follow.getFollowee();
                List<UserPosition> positions = positionsByUserId.getOrDefault(followee.getId(),
                    List.of());
                return SimpleUserInfo.of(followee, positions);
            })
            .toList();
    }

    @Transactional(readOnly = true)
    public List<SimpleUserInfo> getUserFollowers(User user) {
        List<Follow> follows = followRepository.findFollowersWithUser(user);
        List<UUID> userIds = follows.stream()
            .map(follow -> follow.getFollower().getId())
            .toList();

        List<UserPosition> userPositions = userPositionRepository.findByUserIdIn(userIds);

        Map<UUID, List<UserPosition>> positionsByUserId = userPositions.stream()
            .collect(Collectors.groupingBy(
                userPosition -> userPosition.getUser().getId(),
                LinkedHashMap::new,
                Collectors.toList()
            ));

        return follows.stream()
            .map(follow -> {
                User follower = follow.getFollower();
                List<UserPosition> positions = positionsByUserId.getOrDefault(follower.getId(),
                    List.of());
                return SimpleUserInfo.of(follower, positions);
            })
            .toList();
    }
}
