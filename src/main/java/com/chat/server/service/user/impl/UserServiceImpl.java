package com.chat.server.service.user.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.exception.CustomException;
import com.chat.server.common.util.Base64Util;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.common.CommonFileUploadService;
import com.chat.server.service.user.UserService;
import com.chat.server.service.user.response.UserInfoResponse;
import com.chat.server.service.user.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CommonFileUploadService commonFileUploadService;

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

    @Override
    @Transactional
    public void updateProfile(UserId userId,
                              String username,
                              String profileImageUrl,
                              String statusMessage) {
        User user = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        user.updateProfile(username, profileImageUrl, statusMessage);
        userRepository.save(user);
    }

    @Override
    public String uploadProfileImage(UserId userId,
                                     MultipartFile file) {
        String userIdBase64 = Base64Util.encode(userId.toString());
        String filename = UUID.randomUUID().toString().replace("-", "");
        return commonFileUploadService.uploadFile(
                Constants.UPLOAD_SUB_DIR_USER_PROFILE_IMAGE.formatted(userIdBase64),
                file,
                filename,
                Constants.UPLOAD_ALLOWED_EXT_USER_PROFILE,
                Constants.UPLOAD_MAX_BYTES_USER_PROFILE);
    }
}
