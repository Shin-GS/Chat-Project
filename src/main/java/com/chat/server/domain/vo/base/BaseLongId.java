package com.chat.server.domain.vo.base;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;

public abstract class BaseLongId<T extends BaseLongId<T>> implements Comparable<T>, Serializable {
    public abstract Long value();

    @JsonValue
    public Long json() {
        return value();
    }

    @Override
    public final int compareTo(T o) {
        return this.value().compareTo(o.value());
    }

    @Override
    public final boolean equals(Object object) {
        return this == object || (object != null && getClass() == object.getClass() && Objects.equals(this.value(), ((BaseLongId<?>) object).value()));
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), value());
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }
}
