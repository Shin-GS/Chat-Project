package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.sticker.StickerPack;
import com.chat.server.domain.repository.conversation.sticker.StickerPackRepository;
import com.chat.server.domain.repository.conversation.sticker.StickerRepository;
import com.chat.server.service.conversation.ConversationStickerService;
import com.chat.server.service.conversation.response.ConversationStickerPackResponse;
import com.chat.server.service.conversation.response.ConversationStickerPackStickerResponse;
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
    public List<ConversationStickerPackResponse> findPacks() {
        return stickerPackRepository.findAll().stream()
                .filter(pack -> pack.getStickerCount() != 0)
                .sorted(Comparator.comparing(StickerPack::getId))
                .map(ConversationStickerPackResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationStickerPackResponse findPackById(Long packId) {
        return stickerPackRepository.findById(packId)
                .map(ConversationStickerPackResponse::of)
                .orElseThrow(() -> new CustomException(ErrorCode.STICKER_NOT_EXISTS));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationStickerPackStickerResponse> findStickersByPackId(Long packId) {
        StickerPack stickerPack = stickerPackRepository.findById(packId)
                .orElseThrow(() -> new CustomException(ErrorCode.STICKER_NOT_EXISTS));
        return stickerPack.getStickers().stream()
                .map(ConversationStickerPackStickerResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long findFirstStickerPackId() {
        return stickerPackRepository.findFirstByOrderByIdAsc()
                .map(StickerPack::getId)
                .orElseThrow(() -> new CustomException(ErrorCode.STICKER_NOT_EXISTS));
    }
}
