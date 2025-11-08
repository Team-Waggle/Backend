package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.Project;
import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.reference.enums.WorkPeriod;
import com.waggle.domain.reference.enums.WorkWay;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p.createdAt FROM Project p WHERE p.id = :id")
    Optional<LocalDateTime> findCreatedAtById(@Param("id") Long projectId);

    @Query("""
        SELECT p from Project p
        LEFT JOIN FETCH p.user
        WHERE p.id = :projectId
        """)
    Optional<Project> findByIdWithUser(@Param("projectId") Long projectId);

    @Query("""
        SELECT DISTINCT p FROM Project p
        LEFT JOIN FETCH p.user
        LEFT JOIN ProjectRecruitment pr ON p = pr.project
        LEFT JOIN ProjectSkill ps ON p = ps.project
        WHERE (:positions IS NULL OR (pr.position IN :positions AND pr.remainingCount > 0))
        AND (:skills IS NULL OR ps.skill IN :skills)
        AND (:industries IS NULL OR p.industry IN :industries)
        AND (:workPeriods IS NULL OR p.workPeriod IN :workPeriods)
        AND (:workWays IS NULL OR p.workWay IN :workWays)
        AND p.title LIKE CONCAT('%', :query, '%')
        """)
    Page<Project> findWithFilter(
        @Param("positions") Set<Position> positions,
        @Param("skills") Set<Skill> skills,
        @Param("industries") Set<Industry> industries,
        @Param("workPeriods") Set<WorkPeriod> workPeriods,
        @Param("workWays") Set<WorkWay> workWays,
        @Param("query") String query,
        Pageable pageable
    );

    List<Project> findByRecruitmentEndDate(LocalDate date);
}
