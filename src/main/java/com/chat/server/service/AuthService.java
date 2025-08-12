package com.chat.server.service;

import com.chat.server.service.request.SignupRequest;
import com.chat.server.service.request.LoginRequest;
import com.chat.server.service.security.JwtMemberInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    void createUser(SignupRequest request);

    void login(LoginRequest request,
               HttpServletResponse response);

    void logout(HttpServletResponse response);

    JwtMemberInfo getMemberInfo(String token);

    ResponseCookie createTokenCookie(String tokenName,
                                     String token,
                                     long expiredTime);
}
