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

import static com.chat.server.common.constant.FragmentConstants.*;

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
                .addFragment(CONVERSATION_SETTING_MODAL_PATH,
                        CONVERSATION_SETTING_MODAL_FRAGMENT,
                        Map.of(CONVERSATION_SETTING_MODAL_PROFILE, userProfile))
                .build();
    }

    @Operation(summary = "프로필 업데이트")
    @PutMapping("/profile")
    public List<ModelAndView> updateProfile(@ModelAttribute @Valid UserProfileRequest request,
                                            @JwtMember JwtMemberInfo memberInfo) {
        userService.updateProfile(memberInfo.id(), request.username(), request.profileImageUrl(), request.statusMessage());
        UserProfileResponse userProfile = userService.getUserProfile(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.USER_PROFILE_UPDATED)))
                .addFragment(CONVERSATION_SETTING_MODAL_PATH,
                        CONVERSATION_SETTING_MODAL_FRAGMENT,
                        Map.of(CONVERSATION_SETTING_MODAL_PROFILE, userProfile))
                .addFragment(CONVERSATION_USER_MENU_PATH,
                        CONVERSATION_USER_MENU_FRAGMENT,
                        Map.of(CONVERSATION_USER_MENU_USER_INFO, userService.getUserInfo(memberInfo.id())))
                .addFragment(COMMON_MODAL_CLOSE_PATH,
                        COMMON_MODAL_CLOSE_FRAGMENT,
                        COMMON_MODAL_CLOSE_TARGET_ID,
                        COMMON_MODAL_CLOSE_TARGET_MODAL_CONTAINER)
                .build();
    }

    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping(path = "/profile/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ModelAndView> uploadProfileImage(@RequestPart("file") @NotNull MultipartFile file,
                                                 @JwtMember JwtMemberInfo memberInfo) {
        String profileImageUrl = userService.uploadProfileImage(memberInfo.id(), file);
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_SETTING_PROFILE_IMAGE_PATH,
                        CONVERSATION_SETTING_PROFILE_IMAGE_UPLOAD_FRAGMENT,
                        Map.of(CONVERSATION_SETTING_PROFILE_IMAGE_URL, profileImageUrl))
                .addFragment(CONVERSATION_SETTING_PROFILE_IMAGE_PATH,
                        CONVERSATION_SETTING_PROFILE_IMAGE_URL_FRAGMENT,
                        Map.of(CONVERSATION_SETTING_PROFILE_IMAGE_URL, profileImageUrl))
                .build();
    }

    @Operation(summary = "프로필 이미지 삭제")
    @DeleteMapping("/profile/upload")
    public List<ModelAndView> deleteProfileImage() {
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_SETTING_PROFILE_IMAGE_PATH,
                        CONVERSATION_SETTING_PROFILE_IMAGE_UPLOAD_FRAGMENT)
                .addFragment(CONVERSATION_SETTING_PROFILE_IMAGE_PATH,
                        CONVERSATION_SETTING_PROFILE_IMAGE_URL_FRAGMENT)
                .build();
    }
}
