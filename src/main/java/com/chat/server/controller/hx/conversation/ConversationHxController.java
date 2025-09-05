package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations")
public class ConversationHxController {
    private final ConversationService conversationService;
    private final ConversationGroupService conversationGroupService;

    @Operation(summary = "메뉴")
    @GetMapping("/menu")
    public List<ModelAndView> menu(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/menu.html",
                        "components/conversation/menu :: user-menu",
                        Map.of("user", UserInfoResponse.of(memberInfo)))
                .build();
    }

    @Operation(summary = "내 대화방 목록 조회")
    @GetMapping
    public List<ModelAndView> myConversations(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversationService.findConversations(memberInfo.id())))
                .build();
    }

    @Operation(summary = "채팅 패널")
    @GetMapping("/{conversationId}/panel")
    public List<ModelAndView> panel(@PathVariable("conversationId") ConversationId conversationId,
                                    @JwtMember JwtMemberInfo memberInfo) {
        ConversationInfoResponse conversation = conversationService.getAccessibleConversation(conversationId, memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/panel.html",
                        "components/conversation/panel :: conversation-panel",
                        Map.of("user", UserInfoResponse.of(memberInfo),
                                "conversation", conversation))
                .build();
    }

    @Operation(summary = "채팅 참여자 정보 조회")
    @GetMapping("/{conversationId}/participants")
    public List<ModelAndView> participants(@PathVariable("conversationId") ConversationId conversationId,
                                           @JwtMember JwtMemberInfo memberInfo) {
        ConversationInfoResponse conversation = conversationService.getAccessibleConversation(conversationId, memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/participant/list.html",
                        "components/conversation/participant/list :: participant",
                        Map.of("conversationId", conversationId,
                                "type", conversation.type(),
                                "user", UserInfoResponse.of(memberInfo),
                                "nowRole", conversationGroupService.getRole(conversationId, memberInfo.id()),
                                "participants", conversationService.findParticipants(conversationId, memberInfo.id())))
                .build();
    }

    @Operation(summary = "대화방 나가기")
    @PostMapping("/{conversationId}/leave")
    public List<ModelAndView> leave(@PathVariable("conversationId") ConversationId conversationId,
                                    @JwtMember JwtMemberInfo memberInfo) {
        conversationService.leave(memberInfo.id(), conversationId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "Left chat room successfully"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversationService.findConversations(memberInfo.id())))
                .addFragment("templates/components/common/modalClose.html",
                        "components/common/modalClose :: close",
                        "targetId",
                        "conversation-panel")
                .build();
    }
}
