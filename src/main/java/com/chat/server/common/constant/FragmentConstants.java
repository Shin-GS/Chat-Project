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

    public static final String COMMON_MODAL_CLOSE_PATH = "templates/components/common/modalClose.html";
    public static final String COMMON_MODAL_CLOSE_FRAGMENT = "components/common/modalClose :: close";
    public static final String COMMON_MODAL_CLOSE_TARGET_ID = "targetId";
    public static final String COMMON_MODAL_CLOSE_TARGET_MODAL_CONTAINER = "modal-container";
    public static final String COMMON_MODAL_CLOSE_TARGET_CONVERSATION_PANEL = "conversation-panel";
    public static final String COMMON_MODAL_CLOSE_TARGET_SEARCH_FRIEND_LIST =  "search-friend-list";

    public static final String CONVERSATION_USER_MENU_PATH = "templates/components/conversation/menu.html";
    public static final String CONVERSATION_USER_MENU_FRAGMENT = "components/conversation/menu :: user-menu";
    public static final String CONVERSATION_USER_MENU_USER_INFO = "user";

    public static final String CONVERSATION_LIST_PATH = "templates/components/conversation/list.html";
    public static final String CONVERSATION_LIST_FRAGMENT = "components/conversation/list :: conversation-list";
    public static final String CONVERSATION_LIST_CONVERSATION_LIST = "conversations";

    public static final String CONVERSATION_PANEL_PATH = "templates/components/conversation/panel.html";
    public static final String CONVERSATION_PANEL_FRAGMENT = "components/conversation/panel :: conversation-panel";
    public static final String CONVERSATION_PANEL_USER_INFO = "user";
    public static final String CONVERSATION_PANEL_CONVERSATION_INFO = "conversation";

    public static final String CONVERSATION_PARTICIPANT_LIST_PATH = "templates/components/conversation/participant/list.html";
    public static final String CONVERSATION_PARTICIPANT_LIST_FRAGMENT = "components/conversation/participant/list :: participant";
    public static final String CONVERSATION_PARTICIPANT_CONVERSATION_ID = "conversationId";
    public static final String CONVERSATION_PARTICIPANT_CONVERSATION_TYPE = "type";
    public static final String CONVERSATION_PARTICIPANT_CONVERSATION_USER_INFO = "user";
    public static final String CONVERSATION_PARTICIPANT_PARTICIPANT_ROLE = "nowRole";
    public static final String CONVERSATION_PARTICIPANT_CONVERSATION_PARTICIPANT_LIST = "participants";
}
