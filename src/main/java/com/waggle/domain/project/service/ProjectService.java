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

    Project getProjectById(Long projectId);

    ProjectInfo getProjectInfoByProject(Project project);

    Project updateProject(Long projectId, ProjectInputDto projectInputDto, User user);

    void deleteProject(Long projectId, User user);

    List<User> getUsersByProjectId(Long projectId);

    List<User> getAppliedUsersByProjectId(Long projectId);

    List<User> approveAppliedUser(Long projectId, UUID userId, User user);

    List<User> rejectAppliedUser(Long projectId, UUID userId, User user);

    List<User> removeMemberUser(Long projectId, UUID userId, User user);

    void delegateLeader(Long projectId, UUID userId, User user);

    List<Project> getUserProjects(UUID userId);

    void withdrawFromProject(Long projectId, User user);

    Project applyProject(Long projectId, ProjectApplicationDto projectApplicationDto, User user);

    void cancelProjectApplication(Long projectId, User user);

    List<Project> getAppliedProjects(User user);

    boolean toggleCurrentUserBookmark(Long projectId, User user);

    List<Project> getUserBookmarkProjects(UUID userId, Long cursor, int size);

    List<Project> getCurrentUserProjects(User user);
}
