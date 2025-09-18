package com.chat.server.service.conversation;

import com.chat.server.domain.entity.converstaion.sticker.Sticker;
import com.chat.server.service.conversation.response.ConversationStickerPackResponse;
import com.chat.server.service.conversation.response.ConversationStickerPackStickerResponse;

import java.util.List;

public interface ConversationStickerService {
    List<ConversationStickerPackResponse> findPacks();

    ConversationStickerPackResponse findPackById(Long packId);

    List<ConversationStickerPackStickerResponse> findStickersByPackId(Long packId);

    Long findFirstStickerPackId();

    Sticker getStickerById(Long id);
}
