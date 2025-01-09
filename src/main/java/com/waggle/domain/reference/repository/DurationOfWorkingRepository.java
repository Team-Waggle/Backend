package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.DurationOfWorking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DurationOfWorkingRepository extends JpaRepository<DurationOfWorking, Long> {

}
