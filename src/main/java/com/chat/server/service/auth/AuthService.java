package com.chat.server.service.auth;

import com.chat.server.common.CustomException;
import com.chat.server.common.code.ErrorCode;
import com.chat.server.domain.entity.User;
import com.chat.server.domain.entity.UserCredentials;
import com.chat.server.domain.repository.UserRepository;
import com.chat.server.model.request.CreateUserRequest;
import com.chat.server.model.response.CreateUserResponse;
import com.chat.server.security.Hasher;
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
    private final Hasher hasher;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        Optional<User> optionalUser = userRepository.findByName(request.name());
        if (optionalUser.isPresent()) {
            log.error("USER_ALREADY_EXISTS: {}", request.name());
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try {
            User user = User.of(request.name());
            user.setCredentials(UserCredentials.of(user, hasher.getHashingValue(request.password())));

            User savedUser = userRepository.save(user);
            if (ObjectUtils.isEmpty(savedUser)) {
                throw new CustomException(ErrorCode.USER_SAVE_FAILED);
            }

            return new CreateUserResponse(savedUser.getName());

        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_SAVE_FAILED);
        }
    }
}
