package com.chat.server.controller.hx.chat;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.ConversationService;
import com.chat.server.service.UserService;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.response.UserInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
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
    private final ConversationService conversationService;

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

    @Operation(summary = "chatId 이전 이전 메시지 리스트 조회")
    @GetMapping("/{friendUserId}/before")
    public List<ModelAndView> beforeMessages(@PathVariable("friendUserId") Long friendUserId,
                                             @RequestParam(name = "size", defaultValue = "15") int limit,
                                             @RequestParam(name = "chatId", required = false) Long chatId,
                                             @JwtMember JwtMemberInfo memberInfo) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ChatMessageResponse> messages = conversationService.findBeforeChats(memberInfo.id(), friendUserId, chatId, pageable);
        boolean hasMore = !messages.isEmpty();
        Long firstChatId = messages.isEmpty() ? 0L : messages.stream().findFirst().map(ChatMessageResponse::id).orElse(0L);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/chat/before.html",
                        "components/chat/chat/before",
                        Map.of("hasMore", hasMore,
                                "friendUserId", friendUserId,
                                "firstChatId", firstChatId,
                                "messages", messages)
                )
                .build();
    }
}
