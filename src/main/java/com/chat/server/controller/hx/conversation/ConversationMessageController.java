package com.chat.server.controller.hx.conversation;

import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.ConversationStickerService;
import com.chat.server.service.conversation.response.ConversationInfoAndMessageResponse;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import com.chat.server.service.conversation.response.ConversationStickerPackResponse;
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

import static com.chat.server.common.constant.FragmentConstants.*;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations")
public class ConversationMessageController {
    private final ConversationMessageService conversationMessageService;
    private final ConversationService conversationService;
    private final ConversationStickerService conversationStickerService;

    @Operation(summary = "messageId 이전 이전 메시지 리스트 조회")
    @GetMapping("/{conversationId}/messages/before")
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
                .addFragment(CONVERSATION_MESSAGE_BEFORE_PATH,
                        CONVERSATION_MESSAGE_BEFORE_FRAGMENT,
                        Map.of(CONVERSATION_MESSAGE_BEFORE_HAS_NEXT, !messages.isEmpty(),
                                CONVERSATION_MESSAGE_BEFORE_CONVERSATION_ID, conversationId,
                                CONVERSATION_MESSAGE_BEFORE_FIRST_MESSAGE_ID, messages.isEmpty() ? 0L : messages.stream().findFirst().map(ConversationMessageResponse::id).orElse(0L),
                                CONVERSATION_MESSAGE_BEFORE_MESSAGE_LIST, messages))
                .build();
    }

    @Operation(summary = "메시지 읽음 처리")
    @PostMapping("/{conversationId}/messages/read")
    public List<ModelAndView> read(@PathVariable("conversationId") ConversationId conversationId,
                                   @JwtMember JwtMemberInfo memberInfo) {
        conversationMessageService.readMessage(memberInfo.id(), conversationId);
        ConversationInfoAndMessageResponse conversation = conversationService.findConversation(memberInfo.id(), conversationId);
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_MESSAGE_READ_PATH,
                        CONVERSATION_MESSAGE_READ_TITLE_FRAGMENT,
                        Map.of(CONVERSATION_MESSAGE_READ_CONVERSATION, conversation))
                .addFragment(CONVERSATION_MESSAGE_READ_PATH,
                        CONVERSATION_MESSAGE_READ_READ_CHECK_FRAGMENT,
                        Map.of(CONVERSATION_MESSAGE_READ_CONVERSATION, conversation))
                .addFragment(CONVERSATION_MESSAGE_READ_PATH,
                        CONVERSATION_MESSAGE_READ_UN_READ_DOT,
                        Map.of(CONVERSATION_MESSAGE_READ_CONVERSATION, conversation))
                .build();
    }

    @Operation(summary = "메시지 스티커 팩 리스트 조회")
    @GetMapping("/messages/stickers")
    public List<ModelAndView> stickers() {
        List<ConversationStickerPackResponse> allStickerPacks = conversationStickerService.findStickerPacks();
        return new ModelAndViewBuilder()
                .build();
    }
}
