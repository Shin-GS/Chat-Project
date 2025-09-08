package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoAndMessageResponse;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations")
public class ConversationMessageController {
    private final ConversationMessageService conversationMessageService;
    private final ConversationService conversationService;

    @Operation(summary = "messageId 이전 이전 메시지 리스트 조회")
    @GetMapping("/{conversationId}/before")
    public List<ModelAndView> beforeMessages(@PathVariable("conversationId") ConversationId conversationId,
                                             @RequestParam(name = "size", defaultValue = "15") int limit,
                                             @RequestParam(name = "messageId", required = false) Long messageId,
                                             @JwtMember JwtMemberInfo memberInfo) {
        List<ConversationMessageResponse> messages = conversationMessageService.findBeforeMessage(
                memberInfo.id(),
                conversationId,
                messageId,
                PageRequest.of(0, limit));
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/message/before.html",
                        "components/conversation/message/before",
                        Map.of("hasNext", !messages.isEmpty(),
                                "conversationId", conversationId,
                                "firstMessageId", messages.isEmpty() ? 0L : messages.stream().findFirst().map(ConversationMessageResponse::id).orElse(0L),
                                "messages", messages)
                )
                .build();
    }

    @Operation(summary = "메시지 읽음 처리")
    @PostMapping("/{conversationId}/read")
    public List<ModelAndView> read(@PathVariable("conversationId") ConversationId conversationId,
                                   @JwtMember JwtMemberInfo memberInfo) {
        conversationMessageService.read(memberInfo.id(), conversationId);

        ConversationInfoAndMessageResponse conversation = conversationService.findConversation(memberInfo.id(), conversationId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/read.html",
                        "components/conversation/read :: title",
                        Map.of("conversation", conversation))
                .addFragment("templates/components/conversation/read.html",
                        "components/conversation/read :: read-check",
                        Map.of("conversation", conversation))
                .addFragment("templates/components/conversation/read.html",
                        "components/conversation/read :: unread-dot",
                        Map.of("conversation", conversation))
                .build();
    }
}
