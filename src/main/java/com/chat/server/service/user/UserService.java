package com.chat.server.service.user;

import com.chat.server.service.user.response.UserInfoResponse;

import java.util.List;

public interface UserService {
    List<UserInfoResponse> findSimilarNamesExcludingExactMatch(String pattern, Long userId);

    UserInfoResponse findUserInfo(Long userId);
}
