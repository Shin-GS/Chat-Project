package com.chat.server.controller.hx.chat;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.ChatService;
import com.chat.server.service.UserService;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/chats")
public class ChatHxController {
    private final UserService userService;
    private final ChatService chatService;

    @Operation(summary = "채팅 패널")
    @GetMapping("/{friendUserId}/panel")
    public List<ModelAndView> chatPanel(@PathVariable("friendUserId") Long friendUserId,
                                        @JwtMember JwtMemberInfo memberInfo) {
        UserInfoResponse userInfo = UserInfoResponse.of(memberInfo);
        UserInfoResponse friendUserInfo = userService.findUserInfo(friendUserId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/chat/panel.html",
                        "components/chat/chat/panel :: chat-panel",
                        Map.of("user", userInfo, "friendUser", friendUserInfo))
                .build();
    }

    @Operation(summary = "최근 메시지 조회")
    @GetMapping("/{friendUserId}/recent")
    public List<ModelAndView> recentMessage(@PathVariable("friendUserId") Long friendUserId,
                                            @RequestParam(name = "size", defaultValue = "10") int limit,
                                            @JwtMember JwtMemberInfo memberInfo) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ChatMessageResponse> messages = chatService.findRecentChats(memberInfo.id(), friendUserId, pageable);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/chat/list.html",
                        "components/chat/chat/list :: chat-list",
                        Map.of("messages", messages))
                .build();
    }
}
