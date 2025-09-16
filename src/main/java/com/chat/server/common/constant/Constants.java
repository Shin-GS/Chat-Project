package com.chat.server.common.constant;

import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.Set;

@NoArgsConstructor
public class Constants {
    // USER
    public static final int USER_ID_MAX_LENGTH = 512;
    public static final int USER_NAME_MAX_LENGTH = 128;
    public static final int USER_HASHED_PASSWORD_MAX_LENGTH = 512;
    public static final int USER_ROLE_MAX_LENGTH = 50;
    public static final int USER_IMAGE_MAX_LENGTH = 512;
    public static final int USER_STATUS_MESSAGE_MAX_LENGTH = 512;

    // CONVERSATION
    public static final int CONVERSATION_TITLE_MIN_LENGTH = 1;
    public static final int CONVERSATION_TITLE_MAX_LENGTH = 128;
    public static final int CONVERSATION_MESSAGE_MAX_LENGTH = 4000;
    public static final int CONVERSATION_MEMBERSHIP_REASON_MAX_LENGTH = 512;
    public static final int CONVERSATION_IMAGE_MAX_LENGTH = 512;

    // JWT
    public static final String JWT_TOKEN_TYPE = "tokenType";
    public static final String TOKEN_TYPE_ACCESS_TOKEN = "accessToken";
    public static final String TOKEN_TYPE_REFRESH_TOKEN = "refreshToken";
    public static final String JWT_USER_ID = "id";
    public static final String JWT_USER_ACCOUNT_ID = "accountId";
    public static final String JWT_USER_NAME = "userName";
    public static final String JWT_USER_ROLE = "userRole";

    // Hx
    public static final String HX_RELOAD = "HX-RELOAD";
    public static final String HX_REDIRECT = "HX-REDIRECT";

    // Authorization
    public static final String COOKIE_AUTHORIZATION = "Authorization";
    public static final String COOKIE_AUTHORIZATION_REFRESH = "Authorization-Refresh";

    // Response
    public static final String UNAUTHORIZED_RESPONSE_HTML = """
            <html>
              <head>
                <script>
                  alert('%s');
                  window.location.href = '/login';
                </script>
              </head>
              <body></body>
            </html>
            """;
    public static final String UNAUTHORIZED_RESPONSE_HX = """
            <div id="toast-container"
                class="fixed top-4 right-4 space-y-2 z-50"
                hx-swap-oob="true">
                <div class="fixed bottom-5 left-1/2 transform -translate-x-1/2 px-4 py-3 rounded shadow-lg transition-opacity duration-300 text-white bg-red-500">
                    <p>%s</p>
                </div>
            </div>
            """;

    // Socket
    public static final String SOCKET_DESTINATION_CONVERSATION_MESSAGE = "/sub/conversations/%s";
    public static final String SOCKET_DESTINATION_CONVERSATION_SYSTEM_MESSAGE = "/sub/conversations/%s/system";
    public static final String SOCKET_DESTINATION_CONVERSATION_USER_QUEUE_UI = "/queue/ui";

    public static final String USER_UI_REFRESH_IDS = "user-ui-refresh-ids";

    // Cookie
    public static final String COOKIE_LOCALE_NAME = "lang";
    public static final Locale COOKIE_LOCALE_DEFAULT_VALUE = Locale.ENGLISH;

    // Upload
    public static final String UPLOAD_BASE_PATH = "/files";
    public static final String UPLOAD_BASE_DIR = "C:/chat" + UPLOAD_BASE_PATH;
    public static final String UPLOAD_USER_PROFILE_IMAGE_SUB_DIR = "/user/profile/%s";
    public static final Set<String> UPLOAD_USER_PROFILE_IMAGE_ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp");
    public static final long UPLOAD_USER_PROFILE_IMAGE_MAX_BYTES = 2L * 1024 * 1024; // 2MB
    public static final String UPLOAD_CONVERSATION_IMAGE_SUB_DIR = "/user/conversation/%s";
    public static final Set<String> UPLOAD_CONVERSATION_IMAGE_ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp");
    public static final long UPLOAD_CONVERSATION_IMAGE_MAX_BYTES = 2L * 1024 * 1024; // 2MB

    // order
    public static final String ORDER_USER_ID = "userId";
    public static final String ORDER_USER_FRIEND_ID = "userFriendId";
    public static final String ORDER_USER_NAME = "username";
    public static final String ORDER_CONVERSATION_ID = "conversationId";
    public static final String ORDER_CONVERSATION_ACTIVITY_AT = "conversationActivityAt";
}
