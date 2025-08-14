package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.conversation.ConversationFriendService;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.UserService;
import com.chat.server.service.user.response.UserInfoResponse;
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
@RequestMapping("/hx/conversations/friends")
public class ConversationFriendHxController {
    private final UserService userService;
    private final ConversationFriendService conversationFriendService;

    @Operation(summary = "친구 검색 모달")
    @GetMapping("/search/modal")
    public List<ModelAndView> findSimilarUsernamesModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/friend/search/modal.html",
                        "components/conversation/friend/search/modal :: search-modal")
                .build();
    }

    @Operation(summary = "친구 검색 결과 조회")
    @GetMapping("/search")
    public List<ModelAndView> findSimilarUsernames(@RequestParam("keyword") String keyword,
                                                   @JwtMember JwtMemberInfo memberInfo) {
        List<UserInfoResponse> searchUsers = userService.findSimilarNamesExcludingExactMatch(keyword, memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/friend/search/result.html",
                        "components/conversation/friend/search/result :: friend-list",
                        Map.of("searchUsers", searchUsers))
                .build();
    }

    @Operation(summary = "친구 목록 조회")
    @GetMapping
    public List<ModelAndView> findFriends(@JwtMember JwtMemberInfo memberInfo) {
        List<UserInfoResponse> friends = conversationFriendService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/friend/list.html",
                        "components/conversation/friend/list :: friend-list",
                        Map.of("friends", friends))
                .build();
    }

    @Operation(summary = "친구 추가")
    @PostMapping
    public List<ModelAndView> addFriends(@RequestParam("friendUserId") Long friendUserId,
                                         @JwtMember JwtMemberInfo memberInfo) {
        conversationFriendService.addFriend(memberInfo.id(), friendUserId);
        List<UserInfoResponse> friends = conversationFriendService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/friend/list.html",
                        "components/conversation/friend/list :: friend-list",
                        Map.of("friends", friends))
                .addFragment("templates/components/common/modalClose.html",
                        "components/common/modalClose :: close",
                        "targetId",
                        "search-friend-list")
                .build();
    }

    @Operation(summary = "친구 삭제")
    @DeleteMapping
    public List<ModelAndView> removeFriends(@RequestParam("friendUserId") Long friendUserId,
                                            @JwtMember JwtMemberInfo memberInfo) {
        conversationFriendService.removeFriend(memberInfo.id(), friendUserId);
        List<UserInfoResponse> friends = conversationFriendService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/friend/list.html",
                        "components/conversation/friend/list :: friend-list",
                        Map.of("friends", friends))
                .addFragment("templates/components/common/modalClose.html",
                        "components/common/modalClose :: close",
                        "targetId",
                        "search-friend-list")
                .build();
    }
}
