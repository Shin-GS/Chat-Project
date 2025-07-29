package com.chat.server.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
    private RequestUtil() {}

    public static boolean isApiRequest(String uri) {
        return uri != null && uri.startsWith("/api/");
    }

    public static boolean isHxRequest(HttpServletRequest request) {
        return request != null && "true".equals(request.getHeader("HX-Request"));
    }
}
