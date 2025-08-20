package com.chat.server.service.common.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPageRequestDefault {
    int page() default 0;

    int limit() default 10;

    String[] order() default {}; // {"createdAt,desc", "name,asc"}

    int maxLimit() default 100;
}
