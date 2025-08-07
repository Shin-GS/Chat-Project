package com.chat.server.service;

import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.request.CreateUserRequest;
import com.chat.server.service.request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    void createUser(CreateUserRequest request);

    void login(LoginRequest request, HttpServletResponse response);

    void logout(HttpServletResponse response);

    JwtMemberInfo getMemberInfo(String token);

    ResponseCookie createTokenCookie(String tokenName, String token, long expiredTime);
}
