package com.chat.server.controller.hx.chat;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.ConversationService;
import com.chat.server.service.request.conversation.ConversationCreateRequest;
import com.chat.server.service.response.ConversationInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/chats/conversations")
public class ConversationHxController {
    private final ConversationService conversationService;

    @Operation(summary = "대화방 목록 조회")
    @GetMapping
    public List<ModelAndView> findConversations(@JwtMember JwtMemberInfo memberInfo) {
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/conversation/list.html",
                        "components/chat/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
                .build();
    }

    @Operation(summary = "대화방 생성")
    @PostMapping
    public List<ModelAndView> createConversations(@ModelAttribute @Valid ConversationCreateRequest request,
                                                  @JwtMember JwtMemberInfo memberInfo) {
        conversationService.createConversation(memberInfo.id(), request);
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/chat/conversation/list.html",
                        "components/chat/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
                .build();
    }

    // todo 대화방 검색

    @Operation(summary = "대화방 입장")
    @PostMapping("/join")
    public List<ModelAndView> joinConversations(@RequestParam("conversationId") Long conversationId,
                                                @JwtMember JwtMemberInfo memberInfo) {
        conversationService.joinConversationGroup(conversationId, memberInfo.id());
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/chat/conversation/list.html",
                        "components/chat/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
//                .addFragment("templates/components/modalClose.html",
//                        "components/modalClose :: close",
//                        "targetId",
//                        "search-friend-list")
                .build();
    }

    @Operation(summary = "대화방 퇴장")
    @PostMapping("/leave")
    public List<ModelAndView> leaveConversations(@RequestParam("conversationId") Long conversationId,
                                                 @JwtMember JwtMemberInfo memberInfo) {
        conversationService.leaveConversationGroup(conversationId, memberInfo.id());
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/chat/conversation/list.html",
                        "components/chat/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
//                .addFragment("templates/components/modalClose.html",
//                        "components/modalClose :: close",
//                        "targetId",
//                        "search-friend-list")
                .build();
    }
}
