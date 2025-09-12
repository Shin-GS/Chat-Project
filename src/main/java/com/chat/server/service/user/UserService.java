package com.chat.server.service.user;

import com.chat.server.domain.vo.UserId;
import com.chat.server.service.user.response.UserInfoResponse;
import com.chat.server.service.user.response.UserProfileResponse;

public interface UserService {
    UserInfoResponse getUserInfo(UserId userId);

    UserProfileResponse getUserProfile(UserId userId);
}
