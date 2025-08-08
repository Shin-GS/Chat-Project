package com.chat.server.service.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.exception.CustomException;
import com.chat.server.common.util.EncryptUtil;
import com.chat.server.domain.entity.User;
import com.chat.server.domain.entity.UserCredentials;
import com.chat.server.domain.repository.UserRepository;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.security.JwtProperties;
import com.chat.server.service.security.JwtProvider;
import com.chat.server.service.AuthService;
import com.chat.server.service.request.CreateUserRequest;
import com.chat.server.service.request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final EncryptUtil encryptUtil;
    private final JwtProperties jwtProperties;

    @Transactional
    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try {
            userRepository.save(User.of(
                    request.username(),
                    encryptUtil.encode(request.password())
            ));

        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_SAVE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public void login(LoginRequest request,
                      HttpServletResponse response) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        if (!isValidCredentials(user, request.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtProvider.createToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getUsername(), user.getRole());
        response.addHeader(HttpHeaders.SET_COOKIE, createTokenCookie(Constants.COOKIE_AUTHORIZATION, token, jwtProperties.getTokenTime()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, createTokenCookie(Constants.COOKIE_AUTHORIZATION_REFRESH, refreshToken, jwtProperties.getRefreshTokenTime()).toString());
    }

    private boolean isValidCredentials(User user, String rawPassword) {
        UserCredentials userCredentials = user.getUserCredentials();
        return userCredentials != null && encryptUtil.matches(rawPassword, userCredentials.getHashedPassword());
    }

    @Override
    public void logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, createTokenCookie(Constants.COOKIE_AUTHORIZATION, "", 0).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, createTokenCookie(Constants.COOKIE_AUTHORIZATION_REFRESH, "", 0).toString());
    }

    @Override
    public ResponseCookie createTokenCookie(String tokenName, String token, long expiredTime) {
        return ResponseCookie.from(tokenName, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expiredTime)
                .sameSite("Lax")
                .build();
    }

    @Override
    public JwtMemberInfo getMemberInfo(String token) {
        return jwtProvider.getMemberInfo(token);
    }
}
