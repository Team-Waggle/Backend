package com.waggle.domain.projectV2.repository;

import com.waggle.domain.projectV2.ProjectV2;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectV2Repository extends JpaRepository<ProjectV2, UUID> {

    boolean existsByIdNotAndName(UUID id, String name);

    @Query("SELECT COALESCE(MAX(p.sequenceId), 0) FROM ProjectV2 p")
    Long findMaxSequenceId();

    @Query("""
        SELECT p
        FROM ProjectV2 p
        JOIN FETCH p.leader
        WHERE p.id = :id AND p.deletedAt IS NULL
        """)
    Optional<ProjectV2> findByIdWithRelations(@Param("id") UUID id);

    @Query("""
        SELECT p
        FROM ProjectV2 p
        JOIN FETCH p.leader
        WHERE p.deletedAt IS NULL
        ORDER BY p.createdAt DESC, p.sequenceId DESC
        """)
    List<ProjectV2> findAllWithRelations();
}
