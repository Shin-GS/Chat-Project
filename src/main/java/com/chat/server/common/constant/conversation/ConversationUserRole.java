package com.chat.server.common.constant.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConversationUserRole {
    SUPER_ADMIN("최고 관리자", "방 이름 변경, 관리자 지정, 멤버 초대/추방, 공지 작성, 등 모든 권한을 가짐"),
    ADMIN("관리자", "그룹 채팅에서 일부 관리 권한(멤버 초대/추방, 공지 작성 등)을 가진 사용자"),
    MEMBER("참여자", "메시지를 주고받을 수 있지만 방 설정 변경이나 멤버 관리 권한은 없음")
    ;

    private final String roleName;
    private final String description;

    public static ConversationUserRole from(String roleName) {
        if (roleName == null) {
            return null;
        }

        try {
            return ConversationUserRole.valueOf(roleName);

        } catch (Exception e) {
            return null;
        }
    }
}
