package com.chat.server.domain.vo;

import com.chat.server.common.util.VOUtil;
import com.chat.server.domain.vo.base.BaseLongId;
import com.chat.server.domain.vo.base.BaseLongIdDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@JsonDeserialize(using = BaseLongIdDeserializer.class)
public class ConversationId extends BaseLongId<ConversationId> {
    @Column(name = "IGNORED_BY_OVERRIDE")
    private Long value;

    protected ConversationId() {
    }

    public ConversationId(Long value) {
        this.value = VOUtil.positive(value, "ConversationId");
    }

    public static ConversationId of(Long v) {
        return new ConversationId(v);
    }

    @Override
    public Long value() {
        return value;
    }
}
