package com.chat.server.socket.handler.payload;

public record MessagePayload(String to,
                             String from,
                             String message) {

}
