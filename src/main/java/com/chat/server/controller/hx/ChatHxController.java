package com.chat.server.controller.hx;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.security.JwtMember;
import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.ChatService;
import com.chat.server.service.response.UserInfoResponse;
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
@RequestMapping("/hx/chats")
public class ChatHxController {
    private final ChatService chatService;

    @Operation(summary = "친구 목록 조회")
    @GetMapping("/friends")
    public List<ModelAndView> findFriends(@JwtMember JwtMemberInfo memberInfo) {
        List<UserInfoResponse> friends = chatService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/friend/list.html",
                        "components/chat/friend/list :: friend-list",
                        Map.of("friends", friends))
                .build();
    }

    @Operation(summary = "친구 추가")
    @PostMapping("/add-friends")
    public List<ModelAndView> addFriends(@RequestParam("friendUserId") Long friendUserId,
                                         @JwtMember JwtMemberInfo memberInfo) {
        chatService.addFriend(memberInfo.id(), friendUserId);
        List<UserInfoResponse> friends = chatService.findFriends(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/chat/friend/list.html",
                        "components/chat/friend/list :: friend-list",
                        Map.of("friends", friends))
                .build();
    }
}
