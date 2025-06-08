package com.waggle.domain.post.repository;

import com.waggle.domain.post.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user LEFT JOIN FETCH p.project WHERE p.id = :id")
    Optional<Post> findByIdWithRelations(@Param("id") Long postId);

    @Query("SELECT p FROM Post p JOIN FETCH p.user LEFT JOIN FETCH p.project")
    List<Post> findAllWithRelations();
}
