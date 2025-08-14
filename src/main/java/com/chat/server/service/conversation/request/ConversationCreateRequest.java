package com.chat.server.service.conversation.request;

import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.conversation.ConversationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ConversationCreateRequest(@NotBlank
                                        @Size(max = Constants.CONVERSATION_TITLE_MAX_LENGTH)
                                        String title,

                                        @NotBlank
                                        ConversationType type,

                                        Set<Long> userIds) {
}
