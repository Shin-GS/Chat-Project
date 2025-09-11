package com.chat.server.controller.hx.conversation;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationOneToOneService;
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
    private final ConversationOneToOneService conversationOneToOneService;
    private final ConversationService conversationService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "1:1 대화방 들어가기")
    @PostMapping("/{friendUserId}/join")
    public List<ModelAndView> join(@PathVariable("friendUserId") UserId friendUserId,
                                   @JwtMember JwtMemberInfo memberInfo) {
        ConversationId oneToOneConversationId = conversationOneToOneService.join(memberInfo.id(), friendUserId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", codeMessageGetter.getMessage(SuccessCode.CONVERSATION_ONE_TO_ONE_JOINED)))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversationService.findConversations(memberInfo.id())))
                .addFragment("templates/components/conversation/panel.html",
                        "components/conversation/panel :: conversation-panel",
                        Map.of("user", UserInfoResponse.of(memberInfo),
                                "conversation", conversationService.getAccessibleConversation(oneToOneConversationId, memberInfo.id())))
                .build();
    }
}
