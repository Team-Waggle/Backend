package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.entity.User;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project getProjectByProjectId(UUID projectId);

    Project createProject(ProjectInputDto projectInputDto);

    Project updateProject(UUID projectId, ProjectInputDto projectInputDto);

    void deleteProject(UUID projectId);

    Set<User> getUsersByProjectId(UUID projectId);

    Set<User> getAppliedUsersByProjectId(UUID projectId);

    Set<User> approveAppliedUser(UUID projectId, UUID userId);

    Set<User> rejectAppliedUser(UUID projectId, UUID userId);

    Set<User> rejectMemberUser(UUID projectId, UUID userId);

    void delegateLeader(UUID projectId, UUID userId);

    Set<Project> getUserProjects(UUID userId);

    void deleteUserProject(UUID projectId);

    Set<Project> getUserBookmarkProjects(UUID userId);

    Project applyProject(UUID projectId);

    void cancelApplyProject(UUID projectId);

    Set<Project> getAppliedProjects();

    boolean toggleCurrentUserBookmark(UUID projectId);

    Set<Project> getCurrentUserBookmarkProjects();

    Set<Project> getCurrentUserProjects();
}
