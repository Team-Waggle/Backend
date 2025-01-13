package com.waggle.domain.user.service;

import com.waggle.domain.user.dto.UpdateUserDto;
import com.waggle.domain.user.entity.User;

public interface UserService {
    User getCurrentUser();
    User updateUser(UpdateUserDto updateUserDto);
}
