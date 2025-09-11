package com.chat.server.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SupportedLocale {
    EN("en", "English"),
    KO("ko", "Korean");

    private final String code;
    private final String label;
}
