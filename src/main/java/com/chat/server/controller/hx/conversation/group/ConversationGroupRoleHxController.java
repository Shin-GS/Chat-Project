package com.chat.server.controller.hx.conversation.group;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
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
@RequestMapping("/hx/conversations/groups/role")
public class ConversationGroupRoleHxController {
    private final ConversationGroupService conversationGroupService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "그룹 대화방 사용자 룰 변경 모달")
    @GetMapping("/{conversationId}/{userId}/modal")
    public List<ModelAndView> changeRoleModal(@PathVariable("conversationId") ConversationId conversationId,
                                              @PathVariable("userId") UserId userId) {
        ConversationUserRole nowRole = conversationGroupService.getRole(conversationId, userId);
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_GROUP_ROLE_MODAL_PATH,
                        CONVERSATION_GROUP_ROLE_MODAL_FRAGMENT,
                        Map.of(CONVERSATION_GROUP_ROLE_MODAL_CONVERSATION_D, conversationId,
                                CONVERSATION_GROUP_ROLE_MODAL_USER_ID, userId,
                                CONVERSATION_GROUP_ROLE_MODAL_NOW_ROLE, nowRole))
                .build();
    }

    @Operation(summary = "그룹 대화방 사용자 룰 변경")
    @PutMapping("/{conversationId}/{userId}")
    public List<ModelAndView> changeRole(@PathVariable("conversationId") ConversationId conversationId,
                                         @PathVariable("userId") UserId userId,
                                         @RequestParam(name = "role") ConversationUserRole role,
                                         @JwtMember JwtMemberInfo memberInfo) {
        conversationGroupService.changeRole(memberInfo.id(), conversationId, userId, role);
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.CONVERSATION_GROUP_ROLE_UPDATED)))
                .build();
    }
}
