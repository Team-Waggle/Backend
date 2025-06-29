package com.waggle.domain.recruitment.repository;

import com.waggle.domain.recruitment.Recruitment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findByProjectIdOrderByPosition(UUID projectId);
}
