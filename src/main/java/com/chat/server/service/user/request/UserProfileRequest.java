package com.chat.server.service.user.request;

public record UserProfileRequest(String username,
                                 String profileImageUrl,
                                 String statusMessage) {
}
