package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserIndustry;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIndustryRepository extends JpaRepository<UserIndustry, UUID> {

    void deleteByUserId(UUID userId);
}
