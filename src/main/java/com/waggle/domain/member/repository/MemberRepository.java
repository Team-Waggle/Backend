package com.waggle.domain.member.repository;

import com.waggle.domain.member.Member;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
        SELECT m
        FROM Member m
        JOIN FETCH m.user
        JOIN FETCH m.project
        WHERE m.id = :id
        """)
    Optional<Member> findByIdWithRelations(@Param("id") Long id);

    @Query("""
        SELECT m
        FROM Member m
        JOIN FETCH m.user
        JOIN FETCH m.project
        WHERE m.project.id = :projectId
        ORDER BY m.createdAt, m.id
        """)
    List<Member> findByProjectIdWithRelations(@Param("projectId") UUID projectId);
}
