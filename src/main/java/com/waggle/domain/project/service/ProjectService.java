package com.waggle.domain.project.service;

import com.waggle.domain.project.ProjectInfo;
import com.waggle.domain.project.dto.ProjectApplicationDto;
import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.entity.User;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project createProject(ProjectInputDto projectInputDto);

    ProjectInfo getProjectInfoByProjectId(UUID projectId);

    Project updateProject(UUID projectId, ProjectInputDto projectInputDto);

    void deleteProject(UUID projectId);

    Set<User> getUsersByProjectId(UUID projectId);

    Set<User> getAppliedUsersByProjectId(UUID projectId);

    Set<User> approveAppliedUser(UUID projectId, UUID userId);

    Set<User> rejectAppliedUser(UUID projectId, UUID userId);

    Set<User> removeMemberUser(UUID projectId, UUID userId);

    void delegateLeader(UUID projectId, UUID userId);

    Set<Project> getUserProjects(UUID userId);

    void withdrawFromProject(UUID projectId);

    Set<Project> getUserBookmarkProjects(UUID userId);

    Project applyProject(UUID projectId, ProjectApplicationDto projectApplicationDto);

    void cancelProjectApplication(UUID projectId);

    Set<Project> getAppliedProjects();

    boolean toggleCurrentUserBookmark(UUID projectId);

    Set<Project> getCurrentUserBookmarkProjects();

    Set<Project> getCurrentUserProjects();
}
