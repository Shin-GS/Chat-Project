package com.chat.server.service.user;

import com.chat.server.service.user.response.UserInfoResponse;

public interface UserService {
    UserInfoResponse findUserInfo(Long userId);
}
