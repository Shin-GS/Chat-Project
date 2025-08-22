package com.chat.server.service.user.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.user.UserService;
import com.chat.server.service.user.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserInfoResponse findUserInfo(UserId userId) {
        return userRepository.findById(userId.value())
                .map(UserInfoResponse::of)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
    }
}
