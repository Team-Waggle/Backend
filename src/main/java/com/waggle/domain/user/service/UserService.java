package com.waggle.domain.user.service;

import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User updateCurrentUser(MultipartFile profileImage, UserInputDto userInputDto);

    void deleteCurrentUser();

    User getUserByUserId(String userId);
}
