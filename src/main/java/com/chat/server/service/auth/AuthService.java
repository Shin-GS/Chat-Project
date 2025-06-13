package com.chat.server.service.auth;

import com.chat.server.common.CustomException;
import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.util.EncryptUtil;
import com.chat.server.domain.entity.User;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByName(request.name())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try {
            User createdUser = userRepository.save(User.of(
                    request.name(),
                    EncryptUtil.getHashingValue(request.password())
            ));

            if (ObjectUtils.isEmpty(createdUser)) {
                throw new CustomException(ErrorCode.USER_SAVE_FAILED);
            }

            return new CreateUserResponse(createdUser.getName());

        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_SAVE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByName(request.name())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        String requestHashedPassword = EncryptUtil.getHashingValue(request.password());
        if (ObjectUtils.isEmpty(user.getUserCredentials())
                || !user.getUserCredentials().getHashedPassword().equals(requestHashedPassword)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return new LoginResponse(SuccessCode.Success, "token");
    }
}
