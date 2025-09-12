package com.chat.server.controller.hx.conversation;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.UserService;
import com.chat.server.service.user.request.UserProfileRequest;
import com.chat.server.service.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/settings")
public class ConversationSettingHxController {
    private final UserService userService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "세팅 모달")
    @GetMapping("/modal")
    public List<ModelAndView> searchSimilarUsernamesModal(@JwtMember JwtMemberInfo memberInfo) {
        UserProfileResponse userProfile = userService.getUserProfile(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/setting/modal.html",
                        "components/conversation/setting/modal :: setting-modal",
                        Map.of("profile", userProfile))
                .build();
    }

    @Operation(summary = "프로필 업데이트")
    @PutMapping("/profile")
    public List<ModelAndView> updateProfile(@ModelAttribute @Valid UserProfileRequest request,
                                            @JwtMember JwtMemberInfo memberInfo) {
        userService.updateProfile(memberInfo.id(), request.username(), request.profileImageUrl(), request.statusMessage());
        UserProfileResponse userProfile = userService.getUserProfile(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", codeMessageGetter.getMessage(SuccessCode.USER_PROFILE_UPDATED)))
                .addFragment("templates/components/conversation/setting/modal.html",
                        "components/conversation/setting/modal :: setting-modal",
                        Map.of("profile", userProfile))
                .addFragment("templates/components/conversation/menu.html",
                        "components/conversation/menu :: user-menu",
                        Map.of("user", userService.getUserInfo(memberInfo.id())))
                .build();
    }

    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping(path = "/profile/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ModelAndView> uploadProfileImage(@RequestPart("file") @NotNull MultipartFile file,
                                                 @JwtMember JwtMemberInfo memberInfo) {
        String profileImageUrl = userService.uploadProfileImage(memberInfo.id(), file);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-upload",
                        Map.of("profileImageUrl", profileImageUrl))
                .addFragment("templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-url",
                        Map.of("profileImageUrl", profileImageUrl))
                .build();
    }

    @Operation(summary = "프로필 이미지 삭제")
    @DeleteMapping("/profile/upload")
    public List<ModelAndView> deleteProfileImage() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-upload")
                .addFragment("templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-url")
                .build();
    }
}
