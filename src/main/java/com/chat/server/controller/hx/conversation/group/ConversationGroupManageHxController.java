package com.chat.server.controller.hx.conversation.group;

import com.chat.server.common.ModelAndViewBuilder;
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

    @Operation(summary = "그룹 채팅방 생성 모달")
    @GetMapping("/modal")
    public List<ModelAndView> createGroupModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/create/modal.html",
                        "components/conversation/group/create/modal :: create-modal")
                .build();
    }

    @Operation(summary = "그룹 채팅방 생성 - 내 친구 목록 조회")
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
        Long conversationId = conversationGroupService.create(
                memberInfo.id(),
                request.userIds(),
                request.title(),
                request.joinCode(),
                Boolean.TRUE.equals(request.hidden())
        );
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
                .build();
    }

    // 그룹 채팅방 삭제
    // 슈퍼 어드민만 가능
}
