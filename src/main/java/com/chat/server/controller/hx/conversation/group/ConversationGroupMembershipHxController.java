package com.chat.server.controller.hx.conversation.group;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import static com.chat.server.common.constant.FragmentConstants.*;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/groups")
public class ConversationGroupMembershipHxController {
    private final UserService userService;
    private final ConversationGroupService conversationGroupService;
    private final ConversationService conversationService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "그룹 대화방 들어가기 모달")
    @GetMapping("/{conversationId}/join")
    public List<ModelAndView> joinModal(@PathVariable("conversationId") ConversationId conversationId,
                                        @JwtMember JwtMemberInfo memberInfo) {
        ConversationInfoResponse conversation = conversationGroupService.getJoinAbleConversation(conversationId, memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_GROUP_JOIN_MODAL_PATH,
                        CONVERSATION_GROUP_JOIN_MODAL_FRAGMENT,
                        Map.of(CONVERSATION_GROUP_JOIN_MODAL_CONVERSATION, conversation))
                .build();
    }

    @Operation(summary = "그룹 대화방 들어가기")
    @PostMapping("/{conversationId}/join")
    public List<ModelAndView> join(@PathVariable("conversationId") ConversationId conversationId,
                                   @RequestParam(name = "joinCode", required = false) String joinCode,
                                   @JwtMember JwtMemberInfo memberInfo) {
        ConversationId groupConversationId = conversationGroupService.join(memberInfo.id(), joinCode, conversationId);
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.CONVERSATION_GROUP_JOINED)))
                .addFragment(CONVERSATION_LIST_PATH,
                        CONVERSATION_LIST_FRAGMENT,
                        Map.of(CONVERSATION_LIST_CONVERSATION_LIST, conversationService.findConversations(memberInfo.id())))
                .addFragment(CONVERSATION_PANEL_PATH,
                        CONVERSATION_PANEL_FRAGMENT,
                        Map.of(CONVERSATION_PANEL_USER_INFO, userService.getUserInfo(memberInfo.id()),
                                CONVERSATION_PANEL_CONVERSATION_INFO, conversationService.getAccessibleConversation(groupConversationId, memberInfo.id())))
                .addFragment(COMMON_MODAL_CLOSE_PATH,
                        COMMON_MODAL_CLOSE_FRAGMENT,
                        COMMON_MODAL_CLOSE_TARGET_ID,
                        COMMON_MODAL_CLOSE_TARGET_MODAL_CONTAINER)
                .build();
    }
}
