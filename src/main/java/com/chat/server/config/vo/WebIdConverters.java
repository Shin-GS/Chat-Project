package com.chat.server.config.vo;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebIdConverters implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        // String -> UserId
        formatterRegistry.addConverter(String.class, UserId.class, s -> {
            String trimmedValue = s.trim();
            return trimmedValue.isEmpty() ? null : UserId.of(Long.parseLong(trimmedValue));
        });

        // UserId -> String
        formatterRegistry.addConverter(UserId.class, String.class, id ->
                id.value() == null ? null : id.value().toString()
        );

        // String -> ConversationId
        formatterRegistry.addConverter(String.class, ConversationId.class, s -> {
            String trimmedValue = s.trim();
            return trimmedValue.isEmpty() ? null : ConversationId.of(Long.parseLong(trimmedValue));
        });

        // ConversationId -> String
        formatterRegistry.addConverter(ConversationId.class, String.class, id ->
                id.value() == null ? null : id.value().toString()
        );
    }
}
