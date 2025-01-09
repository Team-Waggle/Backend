package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.Industrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustrialRepository extends JpaRepository<Industrial, Long> {

}
