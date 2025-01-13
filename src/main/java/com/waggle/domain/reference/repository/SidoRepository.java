package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SidoRepository extends JpaRepository<Sido, Long> {
}
