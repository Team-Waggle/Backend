package com.waggle.domain.member.repository;

import com.waggle.domain.member.Member;
import com.waggle.domain.projectV2.ProjectV2;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserIdAndProjectId(UUID userId, UUID projectId);

    @Query("""
        SELECT m
        FROM Member m
        JOIN FETCH m.user
        JOIN FETCH m.project
        WHERE m.id = :id AND m.deletedAt IS NULL
        """)
    Optional<Member> findByIdWithRelations(@Param("id") Long id);

    @Query("""
        SELECT m
        FROM Member m
        JOIN FETCH m.user
        JOIN FETCH m.project
        WHERE m.project.id = :projectId AND m.deletedAt IS NULL
        ORDER BY m.createdAt, m.id
        """)
    List<Member> findByProjectIdWithRelations(@Param("projectId") UUID projectId);

    @Query("""
        SELECT m.project
        FROM Member m
        WHERE m.user.id = :userId AND m.deletedAt IS NULL
        ORDER BY m.createdAt DESC, m.id DESC
        """)
    List<ProjectV2> findProjectsByUserId(@Param("userId") UUID userId);

    @Query("""
        SELECT m.project
        FROM Member m
        JOIN FETCH m.project.leader
        WHERE m.user.id = :userId AND m.deletedAt IS NULL
        ORDER BY m.createdAt DESC, m.id DESC
        """)
    List<ProjectV2> findProjectsWithLeaderByUserId(@Param("userId") UUID userId);

    void deleteByUserId(UUID userId);
}
