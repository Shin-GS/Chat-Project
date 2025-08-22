package com.chat.server.domain.vo.base;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Constructor;

@Slf4j
public class BaseLongIdDeserializer<T extends BaseLongId<T>> extends JsonDeserializer<T> implements ContextualDeserializer {
    private final Class<T> target;

    public BaseLongIdDeserializer() {
        this.target = null;
    }

    private BaseLongIdDeserializer(Class<T> target) {
        this.target = target;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty prop) {
        JavaType type = (prop != null) ? prop.getType() : ctx.getContextualType();
        @SuppressWarnings("unchecked")
        Class<T> raw = (Class<T>) type.getRawClass();
        return new BaseLongIdDeserializer<>(raw);
    }

    @Override
    public T deserialize(JsonParser jsonParser,
                         DeserializationContext context) throws IOException {
        if (target == null || jsonParser == null || jsonParser.currentToken() == null) {
            return null;
        }

        JsonToken jsonToken = jsonParser.currentToken();
        return switch (jsonToken) {
            case VALUE_NULL -> null;
            case VALUE_NUMBER_FLOAT ->
                    throw MismatchedInputException.from(jsonParser, target, "Expected integer for %s but got floating number".formatted(target.getSimpleName()));
            case VALUE_NUMBER_INT -> newInstance(jsonParser, jsonParser.getLongValue());
            case VALUE_STRING -> {
                String rawStr = jsonParser.getText();
                if (rawStr == null) {
                    yield null;
                }

                String trimmedStr = rawStr.trim();
                if (trimmedStr.isEmpty()) {
                    yield null;
                }

                try {
                    yield newInstance(jsonParser, Long.parseLong(trimmedStr));

                } catch (NumberFormatException e) {
                    throw MismatchedInputException.from(jsonParser, target, "Invalid long value for %s : %s".formatted(target.getSimpleName(), rawStr));
                }
            }
            default -> throw MismatchedInputException.from(
                    jsonParser,
                    target,
                    "Cannot deserialize %s from token: %s".formatted(target.getSimpleName(), jsonToken));
        };
    }

    private T newInstance(JsonParser jsonParser,
                          long longValue) throws IOException {
        try {
            if (target == null) {
                return null;
            }

            Constructor<T> ctor = target.getDeclaredConstructor(Long.class);
            ctor.setAccessible(true);
            return ctor.newInstance(longValue);

        } catch (NoSuchMethodException e) {
            throw MismatchedInputException.from(
                    jsonParser,
                    target,
                    "Missing (Long) constructor on %s, %s".formatted(target.getSimpleName(), e.getMessage())
            );
        } catch (Exception e) {
            throw MismatchedInputException.from(
                    jsonParser,
                    target,
                    "Failed to construct %s with value %d, %s".formatted(target.getSimpleName(), longValue, e.getMessage())
            );
        }
    }
}
