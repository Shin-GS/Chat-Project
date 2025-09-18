package com.chat.server.service.conversation.response;

import com.chat.server.domain.entity.converstaion.sticker.Sticker;
import com.chat.server.domain.entity.converstaion.sticker.StickerPack;

public record ConversationStickerPackStickerResponse(Long id,
                                                     StickerPack pack,
                                                     String code,
                                                     String imageUrl,
                                                     String altText) {
    public static ConversationStickerPackStickerResponse of(Sticker sticker) {
        return new ConversationStickerPackStickerResponse(sticker.getId(),
                sticker.getPack(),
                sticker.getCode(),
                sticker.getImageUrl(),
                sticker.getAltText());
    }
}
