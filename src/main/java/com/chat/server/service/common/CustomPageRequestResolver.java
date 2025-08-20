package com.chat.server.service.common;

import com.chat.server.service.common.request.CustomPageRequest;
import com.chat.server.service.common.request.CustomPageRequestDefault;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class CustomPageRequestResolver implements HandlerMethodArgumentResolver {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_LIMIT = 5;
    private static final int MAX_LIMIT = 100;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CustomPageRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // Annotation find
        CustomPageRequestDefault requestAnnotation = Optional.ofNullable(parameter.getParameterAnnotation(CustomPageRequestDefault.class))
                .orElse(Optional.ofNullable(parameter.getMethodAnnotation(CustomPageRequestDefault.class))
                        .orElse(parameter.getContainingClass().getAnnotation(CustomPageRequestDefault.class)));

        // Default Value Setting
        int defaultPage = requestAnnotation != null ? requestAnnotation.page() : DEFAULT_PAGE;
        int defaultLimit = requestAnnotation != null ? requestAnnotation.limit() : DEFAULT_LIMIT;
        int defaultMaxLimit = requestAnnotation != null ? requestAnnotation.maxLimit() : MAX_LIMIT;
        List<String> defaultOrder = requestAnnotation != null ? Arrays.asList(requestAnnotation.order()) : List.of();

        // User Request Value Get
        String requestPageStr = trimToNull(webRequest.getParameter("page"));
        String requestLimitStr = trimToNull(webRequest.getParameter("limit"));
        String[] requestOrderArr = webRequest.getParameterValues("order");

        // CustomPageRequest return
        return CustomPageRequest.of(
                Math.max(0, safeParseInt(requestPageStr, defaultPage)),
                getLimit(requestLimitStr, defaultLimit, defaultMaxLimit),
                getOrder(requestOrderArr, defaultOrder)
        );
    }

    private static int getLimit(String requestLimitStr,
                                int defaultLimit,
                                int defaultMaxLimit) {
        if (requestLimitStr == null) {
            return defaultLimit;
        }

        int limit = safeParseInt(requestLimitStr, defaultLimit);
        return limit < 0 ? defaultLimit : Math.min(limit, defaultMaxLimit);
    }

    private static List<String> getOrder(String[] requestOrderArr,
                                         List<String> defaultOrder) {
        if (requestOrderArr == null || requestOrderArr.length == 0) {
            return defaultOrder;
        }

        List<String> orders = Arrays.stream(requestOrderArr)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .toList();
        return orders.isEmpty() ? defaultOrder : orders;
    }

    private static String trimToNull(String str) {
        if (str == null) {
            return null;
        }

        String trimmedStr = str.trim();
        return trimmedStr.isEmpty() ? null : trimmedStr;
    }

    private static int safeParseInt(String str, int defaultValue) {
        try {
            return str == null ? defaultValue : Integer.parseInt(str);

        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
