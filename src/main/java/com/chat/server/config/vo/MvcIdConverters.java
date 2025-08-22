package com.chat.server.config.vo;

import com.chat.server.domain.vo.base.BaseLongId;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Constructor;
import java.util.Set;

// For MVC and Thymeleaf
// MVC - PathVariable, RequestParam, ModelAttribute
// Thymeleaf - @{}
@Configuration
public class MvcIdConverters implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry r) {
        r.addConverter(new StringToAnyId());
        r.addConverter(new AnyIdToString());
    }

    /**
     * String -> Any BaseLongId subclass (UserId, ConversationId, ...)
     */
    static class StringToAnyId implements ConditionalGenericConverter {
        @Override
        public boolean matches(@NonNull TypeDescriptor sourceType,
                               TypeDescriptor targetType) {
            targetType.getObjectType();
            return BaseLongId.class.isAssignableFrom(targetType.getObjectType());
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Set.of(new ConvertiblePair(String.class, BaseLongId.class));
        }

        @Override
        public Object convert(Object source,
                              @NonNull TypeDescriptor sourceType,
                              @NonNull TypeDescriptor targetType) {
            if (source == null) {
                return null;
            }

            String s = source.toString().trim();
            if (s.isEmpty()) {
                return null;
            }

            Long v = Long.valueOf(s);
            Class<?> target = targetType.getObjectType();
            try {
                Constructor<?> ctor = target.getDeclaredConstructor(Long.class);
                ctor.setAccessible(true);
                return ctor.newInstance(v);

            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot construct " + target.getSimpleName() + "(Long). Add a public/protected (Long) ctor.", e);
            }
        }
    }

    /**
     * Any BaseLongId subclass -> String
     */
    static class AnyIdToString implements ConditionalGenericConverter {
        @Override
        public boolean matches(TypeDescriptor sourceType,
                               @NonNull TypeDescriptor targetType) {
            sourceType.getObjectType();
            return BaseLongId.class.isAssignableFrom(sourceType.getObjectType());
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Set.of(new ConvertiblePair(BaseLongId.class, String.class));
        }

        @Override
        public Object convert(Object source,
                              @NonNull TypeDescriptor sourceType,
                              @NonNull TypeDescriptor targetType) {
            if (source == null) {
                return null;
            }

            return ((BaseLongId<?>) source).value() == null ? null : ((BaseLongId<?>) source).value().toString();
        }
    }
}
