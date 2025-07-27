package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserPosition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPositionRepository extends JpaRepository<UserPosition, UUID> {

    List<UserPosition> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
