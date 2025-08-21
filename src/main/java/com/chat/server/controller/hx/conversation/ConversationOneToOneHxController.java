package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/one-to-one")
public class ConversationOneToOneHxController {
    private final ConversationService conversationService;

    @Operation(summary = "1:1 대화방 들어가기")
    @PostMapping("/{friendUserId}/join")
    public List<ModelAndView> joinOneToOne(@PathVariable("friendUserId") Long friendUserId,
                                           @JwtMember JwtMemberInfo memberInfo) {
        Long conversationId = conversationService.joinOneToOne(memberInfo.id(), friendUserId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversationService.findConversations(memberInfo.id())))
                .addFragment("templates/components/conversation/message/panel.html",
                        "components/conversation/message/panel :: conversation-panel",
                        Map.of("user", UserInfoResponse.of(memberInfo),
                                "conversation", conversationService.getConversation(conversationId, memberInfo.id())))
//                .addFragment("templates/components/common/modalClose.html",
//                        "components/common/modalClose :: close",
//                        "targetId",
//                        "search-conversation-group-list")
                .build();
    }

    @Operation(summary = "1:1 대화방 나가기")
    @PostMapping("/{conversationId}/leave")
    public List<ModelAndView> leave(@PathVariable("conversationId") Long conversationId,
                                    @JwtMember JwtMemberInfo memberInfo) {
        conversationService.leaveOneToOne(memberInfo.id(), conversationId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversationService.findConversations(memberInfo.id())))
//                .addFragment("templates/components/common/modalClose.html",
//                        "components/common/modalClose :: close",
//                        "targetId",
//                        "search-friend-list")
                .build();
    }
}
