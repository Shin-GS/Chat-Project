package com.chat.server.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements Code {
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    // Domain
    ENTITY_DELETE_FORBIDDEN(400, "Entity Delete Forbidden"),

    // Request
    UNAUTHORIZED(401, "Unauthorized"),

    // User
    USER_NOT_EXISTS(400, "User not exists"),
    INVALID_PASSWORD(400, "Invalid password"),
    USER_ALREADY_EXISTS(400, "User already exists"),
    USER_SAVE_FAILED(400, "User save failed"),

    // Token
    TOKEN_EMPTY(400, "Token empty"),
    TOKEN_INVALID(400, "Token invalid"),
    TOKEN_EXPIRED(400, "Token expired"),
    TOKEN_NOT_EXPIRED(400, "Token not expired"),

    // Conversation
    CONVERSATION_NOT_EXISTS(400, "Conversation not exists"),
    CONVERSATION_REQUEST_INVALID(400, "conversation request invalid"),
    CONVERSATION_FRIEND_ALREADY_EXISTS(400, "conversation friend already exists"),
    CONVERSATION_FRIEND_NOT_EXISTS(400, "conversation friend not exists"),
    CONVERSATION_GROUP_NOT_EXISTS(400, "conversation group not exists"),
    CONVERSATION_NOT_GROUP(400, "conversation is not a group"),
    CONVERSATION_ALREADY_JOINED(400, "already joined the conversation"),
    CONVERSATION_NOT_MEMBER(400, "conversation is not a member"),
    CONVERSATION_TYPE_NOT_SUPPORTED(400, "conversation type is not supported"),
    CONVERSATION_NAME_REQUIRED(400, "conversation title is required"),
    CONVERSATION_SUPER_ADMIN_REQUIRED(400, "conversation super admin is required"),
    CONVERSATION_NOT_JOINED(400, "conversation is not joined"),
    CONVERSATION_GROUP_ONLY_ALLOWED(400, "conversation group is only allowed"),
    CONVERSATION_ONE_TO_ONE_ONLY_ALLOWED(400, "conversation one to one is only allowed"),
    ;

    private final Integer code;
    private final String message;
}
