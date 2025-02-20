package com.waggle.domain.user.service;

import com.waggle.domain.user.dto.UpdateUserDto;
import com.waggle.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User getCurrentUser();
    User updateUser(MultipartFile profileImage, UpdateUserDto updateUserDto);
    void deleteUser();
    boolean toggleBookmark(String projectId);
}
