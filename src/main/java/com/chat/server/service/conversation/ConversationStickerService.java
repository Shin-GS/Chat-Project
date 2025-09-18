package com.chat.server.service.conversation;

import com.chat.server.service.conversation.response.ConversationStickerPackResponse;

import java.util.List;

public interface ConversationStickerService {
    List<ConversationStickerPackResponse> findStickerPacks();
}
