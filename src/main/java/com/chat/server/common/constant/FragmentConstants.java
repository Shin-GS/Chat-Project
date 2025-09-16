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
    public static final String COMMON_MODAL_CLOSE_TARGET_SEARCH_FRIEND_LIST = "search-friend-list";

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

    public static final String CONVERSATION_GROUP_CREATE_MODAL_PATH = "templates/components/conversation/group/create/modal.html";
    public static final String CONVERSATION_GROUP_CREATE_MODAL_FRAGMENT = "components/conversation/group/create/modal :: create-modal";

    public static final String CONVERSATION_GROUP_CREATE_FRIEND_LIST_PATH = "templates/components/conversation/group/create/friend/list.html";
    public static final String CONVERSATION_GROUP_CREATE_FRIEND_LIST_FRAGMENT = "components/conversation/group/create/friend/list :: create-group-friend-list";
    public static final String CONVERSATION_GROUP_CREATE_FRIEND_LIST_FRIEND = "friends";

    public static final String CONVERSATION_GROUP_CREATE_IMAGE_PATH = "templates/components/conversation/group/create/image.html";
    public static final String CONVERSATION_GROUP_CREATE_IMAGE_UPLOAD_FRAGMENT = "components/conversation/group/create/image :: conversation-image-upload";
    public static final String CONVERSATION_GROUP_CREATE_IMAGE_URL_FRAGMENT = "components/conversation/group/create/image :: conversation-image-url";
    public static final String CONVERSATION_GROUP_CREATE_IMAGE_IMAGE_URL = "imageUrl";

    public static final String CONVERSATION_GROUP_JOIN_MODAL_PATH = "templates/components/conversation/group/join/modal.html";
    public static final String CONVERSATION_GROUP_JOIN_MODAL_FRAGMENT = "components/conversation/group/join/modal :: join-modal";
    public static final String CONVERSATION_GROUP_JOIN_MODAL_CONVERSATION = "conversation";

    public static final String CONVERSATION_GROUP_ROLE_MODAL_PATH = "templates/components/conversation/group/role/modal.html";
    public static final String CONVERSATION_GROUP_ROLE_MODAL_FRAGMENT = "components/conversation/group/role/modal :: role-modal";
    public static final String CONVERSATION_GROUP_ROLE_MODAL_CONVERSATION_D = "conversationId";
    public static final String CONVERSATION_GROUP_ROLE_MODAL_USER_ID = "userId";
    public static final String CONVERSATION_GROUP_ROLE_MODAL_NOW_ROLE = "nowRole";

    public static final String CONVERSATION_GROUP_SEARCH_MODAL_PATH = "templates/components/conversation/group/search/modal.html";
    public static final String CONVERSATION_GROUP_SEARCH_MODAL_FRAGMENT = "components/conversation/group/search/modal :: search-modal";

    public static final String CONVERSATION_GROUP_SEARCH_RESULT_PATH = "templates/components/conversation/group/search/result.html";
    public static final String CONVERSATION_GROUP_SEARCH_RESULT_FRAGMENT = "components/conversation/group/search/result :: conversation-group-list";
    public static final String CONVERSATION_GROUP_SEARCH_RESULT_CONVERSATION_LIST = "conversations";
    public static final String CONVERSATION_GROUP_SEARCH_RESULT_HREF_BASE = "hrefBase";
    public static final String CONVERSATION_GROUP_SEARCH_RESULT_PAGE = "page";

    public static final String CONVERSATION_FRIEND_SEARCH_MODAL_PATH = "templates/components/conversation/friend/search/modal.html";
    public static final String CONVERSATION_FRIEND_SEARCH_MODAL_FRAGMENT = "components/conversation/friend/search/modal :: search-modal";

    public static final String CONVERSATION_FRIEND_SEARCH_MODAL_RESULT_PATH = "templates/components/conversation/friend/search/result.html";
    public static final String CONVERSATION_FRIEND_SEARCH_MODAL_RESULT_FRAGMENT = "components/conversation/friend/search/result :: friend-list";
    public static final String CONVERSATION_FRIEND_SEARCH_MODAL_RESULT_USER_LIST = "searchUsers";

    public static final String CONVERSATION_FRIEND_LIST_PATH = "templates/components/conversation/friend/list.html";
    public static final String CONVERSATION_FRIEND_LIST_FRAGMENT = "components/conversation/friend/list :: friend-list";
    public static final String CONVERSATION_FRIEND_LIST_FRIEND_LIST = "friends";

    public static final String CONVERSATION_FRIEND_PROFILE_MODAL_PATH = "templates/components/conversation/friend/profile/modal.html";
    public static final String CONVERSATION_FRIEND_PROFILE_MODAL_FRAGMENT = "components/conversation/friend/profile/modal :: friend-profile-modal";
    public static final String CONVERSATION_FRIEND_PROFILE_MODAL_MY_USER_ID = "userId";
    public static final String CONVERSATION_FRIEND_PROFILE_MODAL_FRIEND_USER_INFO = "friendUser";
    public static final String CONVERSATION_FRIEND_PROFILE_MODAL_IS_FRIEND = "isFriend";

    public static final String CONVERSATION_MESSAGE_BEFORE_PATH = "templates/components/conversation/message/before.html";
    public static final String CONVERSATION_MESSAGE_BEFORE_FRAGMENT = "components/conversation/message/before";
    public static final String CONVERSATION_MESSAGE_BEFORE_HAS_NEXT = "hasNext";
    public static final String CONVERSATION_MESSAGE_BEFORE_CONVERSATION_ID = "conversationId";
    public static final String CONVERSATION_MESSAGE_BEFORE_FIRST_MESSAGE_ID = "firstMessageId";
    public static final String CONVERSATION_MESSAGE_BEFORE_MESSAGE_LIST = "messages";

    public static final String CONVERSATION_MESSAGE_READ_PATH = "templates/components/conversation/read.html";
    public static final String CONVERSATION_MESSAGE_READ_TITLE_FRAGMENT = "components/conversation/read :: title";
    public static final String CONVERSATION_MESSAGE_READ_READ_CHECK_FRAGMENT = "components/conversation/read :: read-check";
    public static final String CONVERSATION_MESSAGE_READ_UN_READ_DOT = "components/conversation/read :: unread-dot";
    public static final String CONVERSATION_MESSAGE_READ_CONVERSATION = "conversation";

    public static final String CONVERSATION_SETTING_MODAL_PATH = "templates/components/conversation/setting/modal.html";
    public static final String CONVERSATION_SETTING_MODAL_FRAGMENT = "components/conversation/setting/modal :: setting-modal";
    public static final String CONVERSATION_SETTING_MODAL_PROFILE = "profile";

    public static final String CONVERSATION_SETTING_PROFILE_IMAGE_PATH = "templates/components/conversation/setting/profile/image.html";
    public static final String CONVERSATION_SETTING_PROFILE_IMAGE_UPLOAD_FRAGMENT = "components/conversation/setting/profile/image :: profile-image-upload";
    public static final String CONVERSATION_SETTING_PROFILE_IMAGE_URL_FRAGMENT = "components/conversation/setting/profile/image :: profile-image-url";
    public static final String CONVERSATION_SETTING_PROFILE_IMAGE_URL = "profileImageUrl";
}
