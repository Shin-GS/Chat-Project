package com.chat.server.service.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.common.util.EncryptUtil;
import com.chat.server.domain.entity.User;
import com.chat.server.domain.entity.UserCredentials;
import com.chat.server.domain.repository.UserRepository;
import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.request.CreateUserRequest;
import com.chat.server.service.request.LoginRequest;
import com.chat.server.service.response.LoginResponse;
import com.chat.server.security.JwtProvider;
import com.chat.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final EncryptUtil encryptUtil;

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
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        if (!isValidCredentials(user, request.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtProvider.createToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, refreshToken);
    }

    private boolean isValidCredentials(User user, String rawPassword) {
        UserCredentials userCredentials = user.getUserCredentials();
        return userCredentials != null && encryptUtil.matches(rawPassword, userCredentials.getHashedPassword());
    }

    @Override
    public JwtMemberInfo getMemberInfo(String token) {
        return jwtProvider.getMemberInfo(token);
    }
}
