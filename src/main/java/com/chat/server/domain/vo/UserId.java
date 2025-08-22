package com.chat.server.domain.vo;

import com.chat.server.common.util.VOUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class UserId implements Comparable<UserId> {
    // 실제 컬럼명은 @AttributeOverride로 지정
    @Column(name = "IGNORED_BY_OVERRIDE")
    private Long value;

    // for JPA
    protected UserId() {
    }

    public UserId(Long value) {
        this.value = VOUtil.positive(value, "UserId");
    }

    public static UserId of(Long v) {
        return new UserId(v);
    }

    @JsonCreator
    public static UserId fromJson(String raw) {
        return new UserId(Long.valueOf(raw)); // "123" → 123
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
    public int compareTo(UserId o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserId that && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
