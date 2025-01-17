package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.SubIntroduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubIntroduceRepository extends JpaRepository<SubIntroduce, Long> {
}
