package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
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
@RequestMapping("/hx/conversations/group")
public class ConversationGroupHxController {
    private final ConversationService conversationService;

    @Operation(summary = "채팅방 검색 모달")
    @GetMapping("/search/modal")
    public List<ModelAndView> searchConversationModal(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/search/modal.html",
                        "components/conversation/search/modal :: search-modal")
                .build();
    }

    @Operation(summary = "그룹 대화방 생성")
    @PostMapping
    public List<ModelAndView> createGroup(@ModelAttribute @Valid ConversationGroupCreateRequest request,
                                          @JwtMember JwtMemberInfo memberInfo) {
        Long conversationId = conversationService.createGroup(
                memberInfo.id(),
                request.userIds(),
                request.title(),
                request.joinCode(),
                request.hidden()
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

    @Operation(summary = "그룹 대화방 입장")
    @PostMapping("/{conversationId}/join")
    public List<ModelAndView> joinGroup(@PathVariable("conversationId") Long conversationId,
                                        @JwtMember JwtMemberInfo memberInfo) {
        Long groupConversationId = conversationService.joinGroup(conversationId, memberInfo.id());
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
                                "conversation", conversationService.getConversation(groupConversationId, memberInfo.id())))
//                .addFragment("templates/components/common/modalClose.html",
//                        "components/common/modalClose :: close",
//                        "targetId",
//                        "search-friend-list")
                .build();
    }

    @Operation(summary = "대화방 나가기")
    @PostMapping("/{conversationId}/leave")
    public List<ModelAndView> leave(@PathVariable("conversationId") Long conversationId,
                                    @JwtMember JwtMemberInfo memberInfo) {
        conversationService.leave(conversationId, memberInfo.id());
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

    // 그룹 채팅방 삭제
    // 슈퍼 어드민만 가능
}
