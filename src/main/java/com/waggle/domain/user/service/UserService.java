package com.waggle.domain.user.service;

import com.waggle.domain.user.UserInfo;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.entity.User;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User updateCurrentUser(MultipartFile profileImage, UserInputDto userInputDto);

    void deleteCurrentUser();

    UserInfo getUserInfoByUserId(UUID userId);
}
