package com.chat.server.service.response;

public record LoginResponse(String token,
                            String refreshToken) {
}
