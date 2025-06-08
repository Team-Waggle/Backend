package com.waggle.domain.project.service;

import com.waggle.domain.project.ProjectInfo;
import com.waggle.domain.project.dto.ProjectApplicationDto;
import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.entity.User;
import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    Project createProject(ProjectInputDto projectInputDto, User user);

    Project getProjectById(Long projectId);

    ProjectInfo getProjectInfoByProject(Project project);

    Project updateProject(Long projectId, ProjectInputDto projectInputDto, User user);

    void deleteProject(Long projectId, User user);

    Set<User> getUsersByProjectId(Long projectId);

    Set<User> getAppliedUsersByProjectId(Long projectId);

    Set<User> approveAppliedUser(Long projectId, UUID userId, User user);

    Set<User> rejectAppliedUser(Long projectId, UUID userId, User user);

    Set<User> removeMemberUser(Long projectId, UUID userId, User user);

    void delegateLeader(Long projectId, UUID userId, User user);

    Set<Project> getUserProjects(UUID userId);

    void withdrawFromProject(Long projectId, User user);

    Set<Project> getUserBookmarkProjects(UUID userId);

    Project applyProject(Long projectId, ProjectApplicationDto projectApplicationDto, User user);

    void cancelProjectApplication(Long projectId, User user);

    Set<Project> getAppliedProjects(User user);

    boolean toggleCurrentUserBookmark(Long projectId, User user);

    Set<Project> getCurrentUserBookmarkProjects(User user);

    Set<Project> getCurrentUserProjects(User user);
}
