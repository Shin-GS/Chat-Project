package com.chat.server.domain.repository.conversation.sticker;

import com.chat.server.domain.entity.converstaion.sticker.StickerPack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerPackRepository extends JpaRepository<StickerPack, Long> {
}
