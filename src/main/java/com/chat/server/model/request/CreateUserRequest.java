package com.chat.server.model.request;

public record CreateUserRequest(String name,
                                String password) {
}
