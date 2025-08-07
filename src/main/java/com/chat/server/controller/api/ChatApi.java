package com.chat.server.controller.api;

import com.chat.server.common.Response;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.service.ChatService;
import com.chat.server.service.response.ChatMessageResponse;
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

    @Operation(summary = "최근 채팅 메시지 조회")
    @GetMapping("/recent")
    public Response<List<ChatMessageResponse>> getRecentChats(@RequestParam String firstUsername,
                                                              @RequestParam String secondUsername,
                                                              @RequestParam(name = "size", defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return Response.of(SuccessCode.CHATS_RETRIEVED, chatService.findRecentChats(firstUsername, secondUsername, pageable));
    }
}
