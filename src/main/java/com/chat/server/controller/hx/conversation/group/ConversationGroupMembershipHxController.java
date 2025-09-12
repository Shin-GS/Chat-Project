package com.chat.server.controller.hx.conversation.group;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.constant.FragmentConstants;
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
                .addFragment(FragmentConstants.COMMON_TOAST_PATH,
                        FragmentConstants.COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(FragmentConstants.COMMON_TOAST_TYPE, FragmentConstants.COMMON_TOAST_TYPE_SUCCESS,
                                FragmentConstants.COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.CONVERSATION_GROUP_JOINED)))
                .addFragment(FragmentConstants.CONVERSATION_LIST_PATH,
                        FragmentConstants.CONVERSATION_LIST_FRAGMENT,
                        Map.of(FragmentConstants.CONVERSATION_LIST_CONVERSATION_LIST, conversationService.findConversations(memberInfo.id())))
                .addFragment(FragmentConstants.CONVERSATION_PANEL_PATH,
                        FragmentConstants.CONVERSATION_PANEL_FRAGMENT,
                        Map.of(FragmentConstants.CONVERSATION_PANEL_USER_INFO, userService.getUserInfo(memberInfo.id()),
                                FragmentConstants.CONVERSATION_PANEL_CONVERSATION_INFO, conversationService.getAccessibleConversation(groupConversationId, memberInfo.id())))
                .addFragment(FragmentConstants.COMMON_MODAL_CLOSE_PATH,
                        FragmentConstants.COMMON_MODAL_CLOSE_FRAGMENT,
                        FragmentConstants.COMMON_MODAL_CLOSE_TARGET_ID,
                        FragmentConstants.COMMON_MODAL_CLOSE_TARGET_MODAL_CONTAINER)
                .build();
    }
}
