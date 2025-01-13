package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimezoneRepository extends JpaRepository<Timezone, Long> {
}
