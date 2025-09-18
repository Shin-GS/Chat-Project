package com.chat.server.domain.repository.conversation.sticker;

import com.chat.server.domain.entity.converstaion.sticker.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
}
