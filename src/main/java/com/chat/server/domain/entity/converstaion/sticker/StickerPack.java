package com.chat.server.domain.entity.converstaion.sticker;

import com.chat.server.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.chat.server.common.constant.Constants.STICKER_CATEGORY_NAME_MAX_LENGTH;
import static com.chat.server.common.constant.Constants.STICKER_NAME_MAX_LENGTH;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "STICKER_PACK_M",
        indexes = {
                @Index(name = "IX_STICKER_PACK_M_CATEGORY", columnList = "CATEGORY")
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(exclude = "stickers")
public class StickerPack extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PACK_ID")
    private Long id;

    @Column(name = "CATEGORY", length = STICKER_CATEGORY_NAME_MAX_LENGTH, nullable = false, unique = true)
    private String category;

    @Column(name = "NAME", length = STICKER_NAME_MAX_LENGTH)
    private String name;

    @OneToMany(mappedBy = "pack", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortNumber ASC")
    private List<Sticker> stickers = new ArrayList<>();

    public int getStickerCount() {
        return this.stickers == null ? 0 : this.stickers.size();
    }
}
