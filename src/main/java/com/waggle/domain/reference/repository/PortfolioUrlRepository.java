package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.PortfolioUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioUrlRepository extends JpaRepository<PortfolioUrl, Long> {

}
