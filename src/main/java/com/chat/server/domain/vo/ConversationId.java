package com.chat.server.domain.vo;

import com.chat.server.common.util.VOUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ConversationId implements Comparable<ConversationId> {
    // 실제 컬럼명은 @AttributeOverride로 지정
    @Column(name = "IGNORED_BY_OVERRIDE")
    private Long value;

    // for JPA
    protected ConversationId() {
    }

    public ConversationId(Long value) {
        this.value = VOUtil.positive(value, "ConversationId");
    }

    public static ConversationId of(Long v) {
        return new ConversationId(v);
    }

    @JsonCreator
    public static ConversationId fromJson(String raw) {
        return new ConversationId(Long.valueOf(raw)); // "123" → 123
    }

    @JsonValue
    public Long json() {
        return value;
    }

    public Long value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(ConversationId o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ConversationId that && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

