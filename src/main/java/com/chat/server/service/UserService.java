package com.chat.server.service;

import com.chat.server.service.response.UserInfoResponse;

import java.util.List;

public interface UserService {
    List<UserInfoResponse> findSimilarNamesExcludingExactMatch(String pattern, String username);
}
