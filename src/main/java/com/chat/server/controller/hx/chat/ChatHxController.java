package com.chat.server.controller.hx.chat;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.UserService;
import com.chat.server.service.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/chats")
public class ChatHxController {
    private final UserService userService;

    @Operation(summary = "채팅 패널")
    @GetMapping("/{friendUserId}/panel")
    public List<ModelAndView> chatPanel(@PathVariable("friendUserId") Long friendUserId) {
        UserInfoResponse friendUserInfo = userService.findUserInfo(friendUserId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/chat/panel.html",
                        "components/chat/chat/panel :: chat-panel",
                        Map.of("friendUser", friendUserInfo))
                .build();
    }
}
