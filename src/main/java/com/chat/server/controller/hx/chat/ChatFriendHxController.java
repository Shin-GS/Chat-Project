package com.chat.server.controller.hx.chat;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.ConversationService;
import com.chat.server.service.response.UserInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/chats/friends")
public class ChatFriendHxController {
    private final ConversationService conversationService;

    @Operation(summary = "친구 목록 조회")
    @GetMapping
    public List<ModelAndView> findFriends(@JwtMember JwtMemberInfo memberInfo) {
        List<UserInfoResponse> friends = conversationService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/friend/list.html",
                        "components/chat/friend/list :: friend-list",
                        Map.of("friends", friends))
                .build();
    }

    @Operation(summary = "친구 추가")
    @PostMapping
    public List<ModelAndView> addFriends(@RequestParam("friendUserId") Long friendUserId,
                                         @JwtMember JwtMemberInfo memberInfo) {
        conversationService.addFriend(memberInfo.id(), friendUserId);
        List<UserInfoResponse> friends = conversationService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/chat/friend/list.html",
                        "components/chat/friend/list :: friend-list",
                        Map.of("friends", friends))
                .addFragment("templates/components/modalClose.html",
                        "components/modalClose :: close",
                        "targetId",
                        "search-friend-list")
                .build();
    }

    @Operation(summary = "친구 삭제")
    @DeleteMapping
    public List<ModelAndView> removeFriends(@RequestParam("friendUserId") Long friendUserId,
                                            @JwtMember JwtMemberInfo memberInfo) {
        conversationService.removeFriend(memberInfo.id(), friendUserId);
        List<UserInfoResponse> friends = conversationService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/chat/friend/list.html",
                        "components/chat/friend/list :: friend-list",
                        Map.of("friends", friends))
                .addFragment("templates/components/modalClose.html",
                        "components/modalClose :: close",
                        "targetId",
                        "search-friend-list")
                .build();
    }
}
