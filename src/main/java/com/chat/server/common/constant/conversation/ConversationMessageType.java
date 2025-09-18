package com.chat.server.common.constant.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConversationMessageType {
    TEXT("Text Message"),
    IMAGE("Image Message"),
    FILE("File Message"),
    STICKER("Sticker Message"),
    SYSTEM("System Message(join/leave, notice, etc)");

    private final String description;

    public static ConversationMessageType from(String type) {
        if (type == null) {
            return null;
        }

        try {
            return ConversationMessageType.valueOf(type);

        } catch (Exception e) {
            return null;
        }
    }
}

