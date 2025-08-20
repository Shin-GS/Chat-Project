package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.common.request.CustomPageRequest;
import com.chat.server.service.common.request.CustomPageRequestDefault;
import com.chat.server.service.common.response.CustomPageResponse;
import com.chat.server.service.conversation.ConversationFriendService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.request.ConversationGroupCreateRequest;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/groups")
public class ConversationGroupHxController {
    private final ConversationService conversationService;
    private final ConversationFriendService conversationFriendService;

    @Operation(summary = "그룹 채팅방 검색 모달")
    @GetMapping("/search/modal")
    public List<ModelAndView> searchGroupModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/search/modal.html",
                        "components/conversation/group/search/modal :: search-modal")
                .build();
    }

    @Operation(summary = "그룹 채팅방 검색")
    @GetMapping("/search")
    public List<ModelAndView> searchGroup(@RequestParam("keyword") String keyword,
                                          @CustomPageRequestDefault(limit = 5, maxLimit = 50) CustomPageRequest pageRequest,
                                          @JwtMember JwtMemberInfo memberInfo) {
        CustomPageResponse<ConversationInfoResponse> customPageResponse = conversationService.findConversations(memberInfo.id(), keyword, pageRequest.toPageable());
        String hrefBase = UriComponentsBuilder
                .fromPath("/hx/conversations/groups/search")
                .queryParam("keyword", keyword)
                .queryParam("limit", pageRequest.limit())
                .build()
                .toUriString();

        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/search/result.html",
                        "components/conversation/group/search/result :: conversation-group-list",
                        Map.of("conversations", customPageResponse.content(),
                                "hrefBase", hrefBase,
                                "page", customPageResponse))
                .build();
    }

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
        Long conversationId = conversationService.createGroup(
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

    @Operation(summary = "그룹 대화방 나가기")
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
