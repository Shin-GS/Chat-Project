package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationFriendService;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
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
    private final ConversationFriendService conversationFriendService;

    @Operation(summary = "친구 검색 모달")
    @GetMapping("/search/modal")
    public List<ModelAndView> searchSimilarUsernamesModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/friend/search/modal.html",
                        "components/conversation/friend/search/modal :: search-modal")
                .build();
    }

    @Operation(summary = "친구 검색")
    @GetMapping("/search")
    public List<ModelAndView> searchSimilarUsernames(@RequestParam("keyword") String keyword,
                                                     @JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/friend/search/result.html",
                        "components/conversation/friend/search/result :: friend-list",
                        Map.of("searchUsers", conversationFriendService.findFriendsByKeyword(keyword, memberInfo.id())))
                .build();
    }

    @Operation(summary = "내 친구 목록 조회")
    @GetMapping
    public List<ModelAndView> myFriends(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/friend/list.html",
                        "components/conversation/friend/list :: friend-list",
                        Map.of("friends", conversationFriendService.findFriends(memberInfo.id())))
                .build();
    }

    @Operation(summary = "친구 추가")
    @PostMapping
    public List<ModelAndView> addFriends(@RequestParam("friendUserId") UserId friendUserId,
                                         @JwtMember JwtMemberInfo memberInfo) {
        conversationFriendService.addFriend(memberInfo.id(), friendUserId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/friend/list.html",
                        "components/conversation/friend/list :: friend-list",
                        Map.of("friends", conversationFriendService.findFriends(memberInfo.id())))
                .addFragment("templates/components/common/modalClose.html",
                        "components/common/modalClose :: close",
                        "targetId",
                        "search-friend-list")
                .build();
    }

    @Operation(summary = "친구 삭제")
    @DeleteMapping
    public List<ModelAndView> removeFriends(@RequestParam("friendUserId") UserId friendUserId,
                                            @JwtMember JwtMemberInfo memberInfo) {
        conversationFriendService.removeFriend(memberInfo.id(), friendUserId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/friend/list.html",
                        "components/conversation/friend/list :: friend-list",
                        Map.of("friends", conversationFriendService.findFriends(memberInfo.id())))
                .addFragment("templates/components/common/modalClose.html",
                        "components/common/modalClose :: close",
                        "targetId",
                        "search-friend-list")
                .build();
    }
}
