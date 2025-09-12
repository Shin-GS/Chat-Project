package com.chat.server.common.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FragmentConstants {
    public static final String COMMON_TOAST_PATH = "templates/components/common/toast.html";
    public static final String COMMON_TOAST_MESSAGE_FRAGMENT = "components/common/toast :: message";
    public static final String COMMON_TOAST_TYPE = "type";
    public static final String COMMON_TOAST_TYPE_SUCCESS = "success";
    public static final String COMMON_TOAST_TYPE_ERROR = "error";
    public static final String COMMON_TOAST_MESSAGE = "message";

    public static final String CONVERSATION_USER_MENU_PATH = "templates/components/conversation/menu.html";
    public static final String CONVERSATION_USER_MENU_FRAGMENT = "components/conversation/menu :: user-menu";
    public static final String CONVERSATION_USER_MENU_USER_INFO = "user";

    public static final String CONVERSATION_LIST_PATH = "templates/components/conversation/list.html";
    public static final String CONVERSATION_LIST_FRAGMENT = "components/conversation/list :: conversation-list";
    public static final String CONVERSATION_LIST_CONVERSATION_LIST = "conversations";
}
