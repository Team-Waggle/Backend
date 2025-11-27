package com.waggle.domain.follow.repository;

import com.waggle.domain.follow.Follow;
import com.waggle.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowee(User follower, User followee);

    int countByFollower(User follower);

    int countByFollowee(User followee);

    @Query("""
        SELECT f FROM Follow f
        JOIN FETCH f.followee
        WHERE f.follower = :user
        """)
    List<Follow> findFolloweesWithUser(@Param("user") User user);

    @Query("""
        SELECT f FROM Follow f
        JOIN FETCH f.follower
        WHERE f.followee = :user
        """)
    List<Follow> findFollowersWithUser(@Param("user") User user);

    void deleteByFollowerAndFollowee(User follower, User followee);

    void deleteByFollower(User follower);

    void deleteByFollowee(User followee);
}
