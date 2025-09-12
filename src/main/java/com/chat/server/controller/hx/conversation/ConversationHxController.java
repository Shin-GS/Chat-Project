package com.chat.server.controller.hx.conversation;

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
@RequestMapping("/hx/conversations")
public class ConversationHxController {
    private final UserService userService;
    private final ConversationService conversationService;
    private final ConversationGroupService conversationGroupService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "메뉴")
    @GetMapping("/menu")
    public List<ModelAndView> menu(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment(FragmentConstants.CONVERSATION_USER_MENU_PATH,
                        FragmentConstants.CONVERSATION_USER_MENU_FRAGMENT,
                        Map.of(FragmentConstants.CONVERSATION_USER_MENU_USER_INFO, userService.getUserInfo(memberInfo.id())))
                .build();
    }

    @Operation(summary = "내 대화방 목록 조회")
    @GetMapping
    public List<ModelAndView> myConversations(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment(FragmentConstants.CONVERSATION_LIST_PATH,
                        FragmentConstants.CONVERSATION_LIST_FRAGMENT,
                        Map.of(FragmentConstants.CONVERSATION_LIST_CONVERSATION_LIST, conversationService.findConversations(memberInfo.id())))
                .build();
    }

    @Operation(summary = "채팅 패널")
    @GetMapping("/{conversationId}/panel")
    public List<ModelAndView> panel(@PathVariable("conversationId") ConversationId conversationId,
                                    @JwtMember JwtMemberInfo memberInfo) {
        ConversationInfoResponse conversation = conversationService.getAccessibleConversation(conversationId, memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment(FragmentConstants.CONVERSATION_PANEL_PATH,
                        FragmentConstants.CONVERSATION_PANEL_FRAGMENT,
                        Map.of(FragmentConstants.CONVERSATION_PANEL_USER_INFO, userService.getUserInfo(memberInfo.id()),
                                FragmentConstants.CONVERSATION_PANEL_CONVERSATION_INFO, conversation))
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
                                "user", userService.getUserInfo(memberInfo.id()),
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
                .addFragment(FragmentConstants.COMMON_TOAST_PATH,
                        FragmentConstants.COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(FragmentConstants.COMMON_TOAST_TYPE, FragmentConstants.COMMON_TOAST_TYPE_SUCCESS,
                                FragmentConstants.COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.CONVERSATION_LEFT)))
                .addFragment(FragmentConstants.CONVERSATION_LIST_PATH,
                        FragmentConstants.CONVERSATION_LIST_FRAGMENT,
                        Map.of(FragmentConstants.CONVERSATION_LIST_CONVERSATION_LIST, conversationService.findConversations(memberInfo.id())))
                .addFragment("templates/components/common/modalClose.html",
                        "components/common/modalClose :: close",
                        "targetId",
                        "conversation-panel")
                .build();
    }
}
