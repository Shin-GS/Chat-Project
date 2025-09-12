package com.chat.server.controller.hx.conversation.setting;

import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.UserService;
import com.chat.server.service.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/settings")
public class ConversationSettingHxController {
    private final UserService userService;

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
}
