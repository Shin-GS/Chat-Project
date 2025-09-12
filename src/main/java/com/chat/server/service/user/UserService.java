package com.chat.server.service.user;

import com.chat.server.domain.vo.UserId;
import com.chat.server.service.user.response.UserInfoResponse;
import com.chat.server.service.user.response.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserInfoResponse getUserInfo(UserId userId);

    UserProfileResponse getUserProfile(UserId userId);

    void updateProfile(UserId userId,
                       String username,
                       String profileImageUrl,
                       String statusMessage);

    String uploadProfileImage(UserId userId,
                              MultipartFile file);
}
