package com.chat.server.controller.hx.conversation.setting;

import com.chat.server.common.response.ModelAndViewBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/hx/conversations/settings/profile")
public class ConversationSettingProfileHxController {
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ModelAndView> uploadProfileImage(@RequestPart("file") @NotNull MultipartFile file) {
        String url = "https://gw.saraminhr.co.kr/thumb/original/1343945-9820";
        return new ModelAndViewBuilder()
                .addFragment(
                        "templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-upload",
                        Map.of("profileImageUrl", url))
                .addFragment(
                        "templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-url",
                        Map.of("profileImageUrl", url))
                .build();
    }

    @DeleteMapping("/upload")
    public List<ModelAndView> deleteProfileImage() {
        return new ModelAndViewBuilder()
                .addFragment(
                        "templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-upload")
                .addFragment(
                        "templates/components/conversation/setting/profile/image.html",
                        "components/conversation/setting/profile/image :: profile-image-url")
                .build();
    }
}

