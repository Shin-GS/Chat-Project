package com.chat.server.controller.api;

import com.chat.server.common.Response;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.service.ChatService;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatApi {
    private final ChatService chatService;

    @Operation(summary = "chatId 이전 이전 메시지 리스트 조회")
    @GetMapping("/before")
    public Response<List<ChatMessageResponse>> getBeforeChats(@RequestParam String firstUsername,
                                                              @RequestParam String secondUsername,
                                                              @RequestParam(name = "chatId") Long chatId,
                                                              @RequestParam(name = "size", defaultValue = "15") int limit,
                                                              @JwtMember JwtMemberInfo memberInfo) {
        Pageable pageable = PageRequest.of(0, limit);
        return Response.of(SuccessCode.CHATS_RETRIEVED, chatService.findBeforeChats(memberInfo.id(), firstUsername, secondUsername, chatId, pageable));
    }
}
