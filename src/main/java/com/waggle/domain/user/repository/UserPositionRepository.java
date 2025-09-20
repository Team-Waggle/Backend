package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserPosition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPositionRepository extends JpaRepository<UserPosition, UUID> {

    List<UserPosition> findByUserId(UUID userId);

    @Query("""
        SELECT up FROM UserPosition up
        WHERE up.user.id IN :userIds
        ORDER BY up.user.id, up.yearCount DESC
        """)
    List<UserPosition> findByUserIdIn(@Param("userIds") List<UUID> userIds);

    void deleteByUserId(UUID userId);
}
