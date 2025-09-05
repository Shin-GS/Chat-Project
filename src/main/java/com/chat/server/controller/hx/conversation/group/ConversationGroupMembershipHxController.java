package com.chat.server.controller.hx.conversation.group;

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
@RequestMapping("/hx/conversations/groups")
public class ConversationGroupMembershipHxController {
    private final ConversationGroupService conversationGroupService;
    private final ConversationService conversationService;

    @Operation(summary = "그룹 대화방 들어가기 모달")
    @GetMapping("/{conversationId}/join")
    public List<ModelAndView> joinModal(@PathVariable("conversationId") ConversationId conversationId,
                                        @JwtMember JwtMemberInfo memberInfo) {
        ConversationInfoResponse conversation = conversationGroupService.getJoinAbleConversation(conversationId, memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/join/modal.html",
                        "components/conversation/group/join/modal :: join-modal",
                        Map.of("conversation", conversation))
                .build();
    }

    @Operation(summary = "그룹 대화방 들어가기")
    @PostMapping("/{conversationId}/join")
    public List<ModelAndView> join(@PathVariable("conversationId") ConversationId conversationId,
                                   @RequestParam(name = "joinCode", required = false) String joinCode,
                                   @JwtMember JwtMemberInfo memberInfo) {
        ConversationId groupConversationId = conversationGroupService.join(memberInfo.id(), joinCode, conversationId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "Entered chat room successfully"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversationService.findConversations(memberInfo.id())))
                .addFragment("templates/components/conversation/panel.html",
                        "components/conversation/panel :: conversation-panel",
                        Map.of("user", UserInfoResponse.of(memberInfo),
                                "conversation", conversationService.getAccessibleConversation(groupConversationId, memberInfo.id())))
                .addFragment("templates/components/common/modalClose.html",
                        "components/common/modalClose :: close",
                        "targetId",
                        "modal-container")
                .build();
    }
}
