package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.MainIntroduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainIntroduceRepository extends JpaRepository<MainIntroduce, Long> {
}
