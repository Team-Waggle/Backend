package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserJobRole;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJobRoleRepository extends JpaRepository<UserJobRole, UUID> {

    List<UserJobRole> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
