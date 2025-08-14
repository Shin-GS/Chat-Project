package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.user.UserService;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import com.chat.server.service.user.response.UserInfoResponse;
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
@RequestMapping("/hx/conversations")
public class ConversationMessageController {
    private final UserService userService;
    private final ConversationService conversationService;

    @Operation(summary = "채팅 패널")
    @GetMapping("/{conversationId}/panel")
    public List<ModelAndView> chatPanel(@PathVariable("conversationId") Long conversationId,
                                        @JwtMember JwtMemberInfo memberInfo) {
        UserInfoResponse userInfo = UserInfoResponse.of(memberInfo);
        UserInfoResponse friendUserInfo = userService.findUserInfo(conversationId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/message/panel.html",
                        "components/chat/chat/panel :: chat-panel",
                        Map.of("user", userInfo, "friendUser", friendUserInfo))
                .build();
    }

    @Operation(summary = "messageId 이전 이전 메시지 리스트 조회")
    @GetMapping("/{conversationId}/before")
    public List<ModelAndView> beforeMessages(@PathVariable("conversationId") Long conversationId,
                                             @RequestParam(name = "size", defaultValue = "15") int limit,
                                             @RequestParam(name = "messageId", required = false) Long messageId,
                                             @JwtMember JwtMemberInfo memberInfo) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ConversationMessageResponse> messages = conversationService.findBeforeMessage(memberInfo.id(), conversationId, messageId, pageable);
        boolean hasMore = !messages.isEmpty();
        Long firstMessageId = messages.isEmpty() ? 0L : messages.stream().findFirst().map(ConversationMessageResponse::id).orElse(0L);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/message/before.html",
                        "components/chat/chat/before",
                        Map.of("hasMore", hasMore,
                                "friendUserId", conversationId,
                                "firstMessageId", firstMessageId,
                                "messages", messages)
                )
                .build();
    }
}
