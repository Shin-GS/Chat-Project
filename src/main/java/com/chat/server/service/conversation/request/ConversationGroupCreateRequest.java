package com.chat.server.service.conversation.request;

import com.chat.server.common.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ConversationGroupCreateRequest(@NotBlank
                                             @Size(min = Constants.CONVERSATION_TITLE_MIN_LENGTH, max = Constants.CONVERSATION_TITLE_MAX_LENGTH)
                                             String title,

                                             String joinCode,

                                             Boolean hidden,

                                             Set<Long> userIds) {
}
