package com.waggle.domain.reference.repository;

import com.waggle.domain.reference.entity.WaysOfWorking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaysOfWorkingRepository extends JpaRepository<WaysOfWorking, Long> {

}
