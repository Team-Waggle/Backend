package com.waggle.domain.user.service;

import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface UserService {
    User getCurrentUser();
    User updateCurrentUser(MultipartFile profileImage, UserInputDto userInputDto);
    void deleteCurrentUser();
    Set<Project> getCurrentUserProjects();
    boolean toggleCurrentUserBookmark(String projectId);
    Set<Project> getCurrentUserBookmarkProjects();
    User getUserByUserId(String userId);
    Set<Project> getUserProjects(String userId);
    void deleteUserProject(String projectId);
    Set<Project> getUserBookmarkProjects(String userId);
    Project applyProject(String projectId);
    void cancelApplyProject(String projectId);
    Set<Project> getAppliedProjects();
}
