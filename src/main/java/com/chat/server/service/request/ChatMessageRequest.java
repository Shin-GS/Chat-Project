package com.chat.server.service.request;

public record ChatMessageRequest(Long userId,
                                 String message) {
}
