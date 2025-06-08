package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.Project;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByRecruitmentEndDate(LocalDate date);
}
