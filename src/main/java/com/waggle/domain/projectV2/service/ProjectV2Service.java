package com.waggle.domain.projectV2.service;

import com.waggle.domain.member.repository.MemberRepository;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.dto.UpsertProjectDto;
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
    public ProjectV2 createProject(UpsertProjectDto upsertProjectDto, User user) {
        Long maxSequenceId = projectRepository.findMaxSequenceId();

        ProjectV2 project = ProjectV2.builder()
            .name(upsertProjectDto.name())
            .tagline(upsertProjectDto.tagline())
            .description(upsertProjectDto.description())
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
    public ProjectV2 updateProject(UUID projectId, UpsertProjectDto upsertProjectDto, User user) {
        ProjectV2 project = getProject(projectId);

        if (!project.getLeader().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to project with id: " + projectId);
        }

        if (projectRepository.existsByIdNotAndName(projectId, upsertProjectDto.name())) {
            throw new IllegalArgumentException(
                "Project already exists with name: " + upsertProjectDto.name()
            );
        }

        project.update(
            upsertProjectDto.name(),
            upsertProjectDto.tagline(),
            upsertProjectDto.description()
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
