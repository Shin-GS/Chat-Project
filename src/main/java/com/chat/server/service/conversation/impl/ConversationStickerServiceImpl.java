package com.chat.server.service.conversation.impl;

import com.chat.server.domain.entity.converstaion.sticker.StickerPack;
import com.chat.server.domain.repository.conversation.sticker.StickerPackRepository;
import com.chat.server.domain.repository.conversation.sticker.StickerRepository;
import com.chat.server.service.conversation.ConversationStickerService;
import com.chat.server.service.conversation.response.ConversationStickerPackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationStickerServiceImpl implements ConversationStickerService {
    private final StickerPackRepository stickerPackRepository;
    private final StickerRepository stickerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationStickerPackResponse> findStickerPacks() {
        return stickerPackRepository.findAll().stream()
                .filter(pack -> pack.getStickerCount() != 0)
                .sorted(Comparator.comparing(StickerPack::getId))
                .map(ConversationStickerPackResponse::of)
                .toList();
    }
}
