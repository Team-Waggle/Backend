package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserIntroduction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIntroductionRepository extends JpaRepository<UserIntroduction, UUID> {

    void deleteByUserId(UUID userId);
}
