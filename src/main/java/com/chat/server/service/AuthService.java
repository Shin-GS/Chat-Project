package com.chat.server.service;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.common.util.EncryptUtil;
import com.chat.server.domain.entity.User;
import com.chat.server.domain.entity.UserCredentials;
import com.chat.server.domain.repository.UserRepository;
import com.chat.server.model.request.CreateUserRequest;
import com.chat.server.model.request.LoginRequest;
import com.chat.server.model.response.LoginResponse;
import com.chat.server.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final EncryptUtil encryptUtil;

    @Transactional
    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByName(request.name())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try {
            userRepository.save(User.of(
                    request.name(),
                    encryptUtil.encode(request.password())
            ));

        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_SAVE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByName(request.name())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        if (!isValidCredentials(user, request.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtProvider.createToken(user.getId(), user.getName());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getName());
        return new LoginResponse(token, refreshToken);
    }

    private boolean isValidCredentials(User user, String rawPassword) {
        UserCredentials userCredentials = user.getUserCredentials();
        return userCredentials != null && encryptUtil.matches(rawPassword, userCredentials.getHashedPassword());
    }

    public Long getUserIdFromToken(String token) {
        return jwtProvider.getUserIdFromToken(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtProvider.getUsernameFromToken(token);
    }
}
