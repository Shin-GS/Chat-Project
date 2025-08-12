package com.chat.server.service.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.exception.CustomException;
import com.chat.server.common.util.EncryptUtil;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.service.AuthService;
import com.chat.server.service.request.SignupRequest;
import com.chat.server.service.request.LoginRequest;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.security.JwtProperties;
import com.chat.server.service.security.JwtProvider;
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
    public void createUser(SignupRequest request) {
        if (userRepository.existsByAccountId(request.accountId())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try {
            userRepository.save(User.ofUser(
                    request.accountId(),
                    encryptUtil.encode(request.password()),
                    request.username())
            );

        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_SAVE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public void login(LoginRequest request,
                      HttpServletResponse response) {
        User user = userRepository.findByAccountId(request.accountId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        if (!isValidCredentials(user, request.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtProvider.createToken(user.getId(), user.getAccountId(), user.getUsername(), user.getRole());
        response.addHeader(HttpHeaders.SET_COOKIE,
                createTokenCookie(Constants.COOKIE_AUTHORIZATION, token, jwtProperties.getTokenTime()).toString());

        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getAccountId(), user.getUsername(), user.getRole());
        response.addHeader(HttpHeaders.SET_COOKIE,
                createTokenCookie(Constants.COOKIE_AUTHORIZATION_REFRESH, refreshToken, jwtProperties.getRefreshTokenTime()).toString());
    }

    private boolean isValidCredentials(User user, String rawPassword) {
        String hashedPassword = user.getHashedPassword();
        return hashedPassword != null && encryptUtil.matches(rawPassword, hashedPassword);
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
