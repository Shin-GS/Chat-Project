package com.chat.server.service.auth;

import com.chat.server.common.CustomException;
import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.util.EncryptUtil;
import com.chat.server.domain.entity.User;
import com.chat.server.domain.entity.UserCredentials;
import com.chat.server.domain.repository.UserRepository;
import com.chat.server.model.request.CreateUserRequest;
import com.chat.server.model.request.LoginRequest;
import com.chat.server.model.response.CreateUserResponse;
import com.chat.server.model.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        Optional<User> optionalUser = userRepository.findByName(request.name());
        if (optionalUser.isPresent()) {
            log.error("USER_ALREADY_EXISTS: {}", request.name());
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try {
            User user = User.of(request.name());
            user.setCredentials(UserCredentials.of(user, EncryptUtil.getHashingValue(request.password())));

            User savedUser = userRepository.save(user);
            if (ObjectUtils.isEmpty(savedUser)) {
                throw new CustomException(ErrorCode.USER_SAVE_FAILED);
            }

            return new CreateUserResponse(savedUser.getName());

        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_SAVE_FAILED);
        }
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByName(request.name());
        if (optionalUser.isPresent()) {
            log.error("USER_NOT_EXISTS: {}", request.name());
            throw new CustomException(ErrorCode.USER_NOT_EXISTS);
        }

        optionalUser.map(user -> {
                    String hashingValue = EncryptUtil.getHashingValue(request.password());
                    if (!hashingValue.equals(user.getUserCredentials().getHashed_password())) {
                        throw new CustomException(ErrorCode.INVALID_PASSWORD);
                    }
                    return user;
                })
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PASSWORD));

        return new LoginResponse(SuccessCode.Success, "token");
    }
}
