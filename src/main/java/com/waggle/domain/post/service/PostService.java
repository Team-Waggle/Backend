package com.waggle.domain.post.service;

import com.waggle.domain.post.Post;
import com.waggle.domain.post.dto.UpsertPostDto;
import com.waggle.domain.post.repository.PostRepository;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.repository.ProjectV2Repository;
import com.waggle.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ProjectV2Repository projectRepository;

    @Transactional
    public Post createPost(UpsertPostDto upsertPostDto, User user) {
        ProjectV2 project = Optional.ofNullable(upsertPostDto.projectId())
            .map(id -> projectRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id))
            )
            .orElse(null);

        // TODO: 프로젝트 멤버 검증

        Post post = Post.builder()
            .title(upsertPostDto.title())
            .content(upsertPostDto.content())
            .user(user)
            .project(project)
            .build();

        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Post getPost(Long postId) {
        return postRepository.findByIdWithRelations(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
    }

    @Transactional
    public Post updatePost(Long postId, UpsertPostDto upsertPostDto, User user) {
        Post post = getPost(postId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to post with id: " + postId);
        }

        ProjectV2 project = Optional.ofNullable(upsertPostDto.projectId())
            .map(id -> projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id))
            )
            .orElse(null);

        // TODO: 프로젝트 멤버 검증

        post.update(upsertPostDto.title(), upsertPostDto.content(), project);

        return post;
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = getPost(postId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to post with id: " + postId);
        }

        post.delete();
    }
}
