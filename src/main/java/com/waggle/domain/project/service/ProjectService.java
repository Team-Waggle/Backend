package com.waggle.domain.project.service;

import com.waggle.domain.project.ProjectInfo;
import com.waggle.domain.project.dto.ProjectApplicationDto;
import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Project createProject(ProjectInputDto projectInputDto, User user);

    Page<Project> getProjects(Pageable pageable);

    Project getProjectById(UUID projectId);

    ProjectInfo getProjectInfoByProject(Project project);

    Project updateProject(UUID projectId, ProjectInputDto projectInputDto, User user);

    void deleteProject(UUID projectId, User user);

    List<User> getUsersByProjectId(UUID projectId);

    List<User> getAppliedUsersByProjectId(UUID projectId);

    List<User> approveAppliedUser(UUID projectId, UUID userId, User user);

    List<User> rejectAppliedUser(UUID projectId, UUID userId, User user);

    List<User> removeMemberUser(UUID projectId, UUID userId, User user);

    void delegateLeader(UUID projectId, UUID userId, User user);

    List<Project> getUserProjects(UUID userId);

    void withdrawFromProject(UUID projectId, User user);

    List<Project> getUserBookmarkProjects(UUID userId);

    Project applyProject(UUID projectId, ProjectApplicationDto projectApplicationDto, User user);

    void cancelProjectApplication(UUID projectId, User user);

    List<Project> getAppliedProjects(User user);

    boolean toggleCurrentUserBookmark(UUID projectId, User user);

    List<Project> getCurrentUserBookmarkProjects(User user);

    List<Project> getCurrentUserProjects(User user);
}
