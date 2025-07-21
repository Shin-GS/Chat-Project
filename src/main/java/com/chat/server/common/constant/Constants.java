package com.chat.server.common.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Constants {
    // JWT
    public static final String JWT_TOKEN_TYPE = "tokenType";
    public static final String TOKEN_TYPE_ACCESS_TOKEN = "accessToken";
    public static final String TOKEN_TYPE_REFRESH_TOKEN = "refreshToken";
    public static final String JWT_USER_ID = "userId";
    public static final String JWT_USER_NAME = "userName";
    public static final String JWT_USER_ROLE = "userRole";

    // Authorization
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_REFRESH = "Authorization-Refresh";
    public static final String HEADER_AUTHORIZATION_INVALID = "HX-Token-Invalid";

    // Swagger
    public static final String SWAGGER_ACCESS_TOKEN = "accessToken";
    public static final String SWAGGER_REFRESH_TOKEN = "refreshToken";
}
