package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserSkill;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, UUID> {

    List<UserSkill> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
