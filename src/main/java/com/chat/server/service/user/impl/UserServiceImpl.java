package com.chat.server.service.user.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.user.UserService;
import com.chat.server.service.user.response.UserInfoResponse;
import com.chat.server.service.user.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(UserId userId) {
        return userRepository.findById(userId.value())
                .map(UserInfoResponse::of)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(UserId userId) {
        return userRepository.findById(userId.value())
                .map(UserProfileResponse::of)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
    }
}
