package com.chat.server.service.conversation.response;

import com.chat.server.domain.entity.converstaion.sticker.StickerPack;

public record ConversationStickerPackResponse(Long id,
                                              String category,
                                              String name,
                                              int count) {
    public static ConversationStickerPackResponse of(StickerPack stickerPack) {
        return new ConversationStickerPackResponse(
                stickerPack.getId(),
                stickerPack.getCategory(),
                stickerPack.getName(),
                stickerPack.getStickerCount());
    }
}
