package com.chat.server.service;

import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.request.CreateUserRequest;
import com.chat.server.service.request.LoginRequest;
import com.chat.server.service.response.LoginResponse;

public interface AuthService {
    void createUser(CreateUserRequest request);

    LoginResponse login(LoginRequest request);

    JwtMemberInfo getMemberInfo(String token);
}
