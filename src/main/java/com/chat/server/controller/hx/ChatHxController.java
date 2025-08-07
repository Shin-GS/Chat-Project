package com.chat.server.controller.hx;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.security.JwtMember;
import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/chats")
public class ChatHxController {
    private final ChatService chatService;

    @Operation(summary = "친구 추가")
    @PostMapping("/add-friends")
    public List<ModelAndView> addFriends(@RequestParam("friendId") Long friendId,
                                         @JwtMember JwtMemberInfo memberInfo) {
        chatService.addFriend(memberInfo.id(), friendId);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .build();
    }
}
