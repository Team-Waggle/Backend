package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserSkill;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, UUID> {

    void deleteByUserId(UUID userId);
}
