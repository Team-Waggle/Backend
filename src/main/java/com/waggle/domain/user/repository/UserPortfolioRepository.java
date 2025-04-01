package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.UserPortfolio;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, UUID> {

    void deleteByUserId(UUID userId);
}
