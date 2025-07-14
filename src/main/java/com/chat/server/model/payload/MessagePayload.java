package com.chat.server.model.payload;

public record MessagePayload(String to,
                             String from,
                             String message) {

}
