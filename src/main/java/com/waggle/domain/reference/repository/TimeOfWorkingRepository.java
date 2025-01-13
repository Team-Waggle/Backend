package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.TimeOfWorking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeOfWorkingRepository extends JpaRepository<TimeOfWorking, Long> {
}
