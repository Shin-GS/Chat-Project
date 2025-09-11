package com.chat.server.controller.hx.conversation.group;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.service.conversation.ConversationFriendService;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.request.ConversationGroupCreateRequest;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/groups")
public class ConversationGroupManageHxController {
    private final ConversationService conversationService;
    private final ConversationGroupService conversationGroupService;
    private final ConversationFriendService conversationFriendService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "그룹 대화방 생성 모달")
    @GetMapping("/modal")
    public List<ModelAndView> createGroupModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/create/modal.html",
                        "components/conversation/group/create/modal :: create-modal")
                .build();
    }

    @Operation(summary = "그룹 대화방 생성 - 내 친구 목록 조회")
    @GetMapping("/modal/friends")
    public List<ModelAndView> createGroupModalFriends(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/create/friend/list.html",
                        "components/conversation/group/create/friend/list :: create-group-friend-list",
                        Map.of("friends", conversationFriendService.findFriends(memberInfo.id())))
                .build();
    }

    @Operation(summary = "그룹 대화방 생성")
    @PostMapping
    public List<ModelAndView> createGroup(@ModelAttribute @Valid ConversationGroupCreateRequest request,
                                          @JwtMember JwtMemberInfo memberInfo) {
        ConversationId conversationId = conversationGroupService.create(
                memberInfo.id(),
                request.userIds(),
                request.title(),
                request.joinCode(),
                Boolean.TRUE.equals(request.hidden())
        );
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", codeMessageGetter.getMessage(SuccessCode.CONVERSATION_GROUP_CREATED)))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversationService.findConversations(memberInfo.id())))
                .addFragment("templates/components/conversation/panel.html",
                        "components/conversation/panel :: conversation-panel",
                        Map.of("user", UserInfoResponse.of(memberInfo),
                                "conversation", conversationService.getAccessibleConversation(conversationId, memberInfo.id())))
                .build();
    }

    // 그룹 대화방 삭제
    // 슈퍼 어드민만 가능
}
