package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.Project;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p.createdAt FROM Project p WHERE p.id = :id")
    Optional<LocalDateTime> findCreatedAtById(@Param("id") Long projectId);

    List<Project> findByRecruitmentEndDate(LocalDate date);
}
