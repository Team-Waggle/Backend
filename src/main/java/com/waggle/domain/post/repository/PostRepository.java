package com.waggle.domain.post.repository;

import com.waggle.domain.post.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT p
        FROM Post p
        JOIN FETCH p.user
        LEFT JOIN FETCH p.project
        WHERE p.id = :id AND p.deletedAt IS NULL
        """)
    Optional<Post> findByIdWithRelations(@Param("id") Long id);

    @Query("""
        SELECT p
        FROM Post p
        JOIN FETCH p.user
        LEFT JOIN FETCH p.project
        WHERE p.deletedAt IS NULL
        ORDER BY p.createdAt DESC, p.id DESC
        """)
    List<Post> findAllWithRelations();

    @Query("""
        SELECT p
        From Post p
        JOIN FETCH p.user
        LEFT JOIN FETCH p.project
        WHERE p.id IN :ids AND p.deletedAt IS NULL
        ORDER BY p.createdAt DESC, p.id DESC
        """)
    List<Post> findAllByIdInWithRelations(@Param("ids") List<Long> ids);
}
