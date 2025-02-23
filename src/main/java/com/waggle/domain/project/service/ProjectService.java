package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.entity.User;

import java.util.Set;
import java.util.UUID;

public interface ProjectService {
    Project getProjectByProjectId(UUID id);
    Project createProject(ProjectInputDto projectInputDto);
    Project updateProject(UUID id, ProjectInputDto projectInputDto);
    void deleteProject(UUID id);
    Set<User> getUsersByProjectId(UUID id);
    Set<User> getAppliedUsersByProjectId(UUID id);
    Set<User> approveAppliedUser(UUID projectId, String userId);
    Set<User> rejectAppliedUser(UUID projectId, String userId);
    Set<User> rejectMemberUser(UUID projectId, String userId);
    void delegateLeader(UUID projectId, String userId);
}
