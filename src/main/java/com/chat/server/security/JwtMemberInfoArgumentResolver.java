package com.chat.server.security;

import com.chat.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class JwtMemberInfoArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenResolver tokenResolver;
    private final AuthService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtMember.class) && parameter.getParameterType().equals(JwtMemberInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String token = tokenResolver.resolveAccessToken();
        Long userId = authService.getUserIdFromToken(token);
        String username = authService.getUsernameFromToken(token);
        return new JwtMemberInfo(userId, username);
    }
}
