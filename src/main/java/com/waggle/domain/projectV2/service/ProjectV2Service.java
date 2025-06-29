package com.waggle.domain.projectV2.service;

import com.waggle.domain.member.repository.MemberRepository;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.dto.UpsertProjectRequest;
import com.waggle.domain.projectV2.repository.ProjectV2Repository;
import com.waggle.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectV2Service {

    private final MemberRepository memberRepository;
    private final ProjectV2Repository projectRepository;

    @Transactional
    public ProjectV2 createProject(UpsertProjectRequest upsertProjectRequest, User user) {
        Long maxSequenceId = projectRepository.findMaxSequenceId();

        ProjectV2 project = ProjectV2.builder()
            .name(upsertProjectRequest.name())
            .tagline(upsertProjectRequest.tagline())
            .description(upsertProjectRequest.description())
            .sequenceId(maxSequenceId + 1)
            .leader(user)
            .build();

        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public ProjectV2 getProject(UUID projectId) {
        return projectRepository.findByIdWithRelations(projectId)
            .orElseThrow(
                () -> new EntityNotFoundException("Project not found with id: " + projectId)
            );
    }

    @Transactional(readOnly = true)
    public List<ProjectV2> getUserProjects(UUID userId) {
        return memberRepository.findProjectsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ProjectV2> getProjects() {
        return projectRepository.findAllWithRelations();
    }

    @Transactional
    public ProjectV2 updateProject(UUID projectId, UpsertProjectRequest upsertProjectRequest,
        User user) {
        ProjectV2 project = getProject(projectId);

        if (!project.getLeader().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to project with id: " + projectId);
        }

        if (projectRepository.existsByIdNotAndName(projectId, upsertProjectRequest.name())) {
            throw new IllegalArgumentException(
                "Project already exists with name: " + upsertProjectRequest.name()
            );
        }

        project.update(
            upsertProjectRequest.name(),
            upsertProjectRequest.tagline(),
            upsertProjectRequest.description()
        );

        return project;
    }

    @Transactional
    public void deleteProject(UUID projectId, User user) {
        ProjectV2 project = getProject(projectId);

        if (!project.getLeader().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to project with id: " + projectId);
        }

        project.delete();
    }
}
