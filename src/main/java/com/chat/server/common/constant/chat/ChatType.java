package com.chat.server.common.constant.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatType {
    ONE_TO_ONE("1:1 채팅"),
    GROUP("그룹 채팅")
    // 문의, 광고 등도 추가 가능성 있음
    ;

    private final String description;

    public static ChatType from(String roleName) {
        if (roleName == null) {
            return null;
        }

        try {
            return ChatType.valueOf(roleName);

        } catch (Exception e) {
            return null;
        }
    }
}
