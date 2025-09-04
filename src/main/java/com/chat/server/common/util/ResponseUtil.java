package com.chat.server.common.util;

import com.chat.server.common.Response;
import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResponseUtil {
    private final ObjectMapper objectMapper;
    private final AuthService authService;

    public void unAuthorizationResponse(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Exception exception) throws IOException {
        if (RequestUtil.isHxRequest(request)) {
            authService.logout(response);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(Constants.UNAUTHORIZED_RESPONSE_HX.formatted(exception.getMessage()));
            response.setHeader(Constants.HX_REDIRECT, "/login");
            return;
        }

        if (RequestUtil.isApiRequest(request.getRequestURI())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            String jsonResponse = objectMapper.writeValueAsString(Response.of(ErrorCode.UNAUTHORIZED));
            response.getWriter().write(jsonResponse);
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(Constants.UNAUTHORIZED_RESPONSE_HTML);
    }
}
