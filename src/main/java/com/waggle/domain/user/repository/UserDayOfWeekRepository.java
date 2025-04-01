package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserDayOfWeek;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDayOfWeekRepository extends JpaRepository<UserDayOfWeek, UUID> {

    void deleteByUserId(UUID userId);
}
