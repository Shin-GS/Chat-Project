package com.chat.server.domain.vo;

import com.chat.server.common.util.VOUtil;
import com.chat.server.domain.vo.base.BaseLongId;
import com.chat.server.domain.vo.base.BaseLongIdDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@JsonDeserialize(using = BaseLongIdDeserializer.class)
public class UserId extends BaseLongId<UserId> {
    @Column(name = "IGNORED_BY_OVERRIDE")
    private Long value;

    protected UserId() {
    }

    public UserId(Long value) {
        this.value = VOUtil.positive(value, "UserId");
    }

    public static UserId of(Long v) {
        return new UserId(v);
    }

    @Override
    public Long value() {
        return value;
    }
}
