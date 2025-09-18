package com.chat.server.domain.entity.converstaion.sticker;

import com.chat.server.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static com.chat.server.common.constant.Constants.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "STICKER_D",
        indexes = {
                @Index(name = "IX_STICKER_D_PACK_ID", columnList = "PACK_ID"),
                @Index(name = "IX_STICKER_D_CODE", columnList = "STICKER_CODE")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_STICKER_D_PACK_CODE",
                        columnNames = {"PACK_ID", "STICKER_CODE"}
                )
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(exclude = "pack")
public class Sticker extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STICKER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PACK_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_STICKER_PACK"))
    private StickerPack pack;

    @Column(name = "STICKER_CODE", length = STICKER_CODE_MAX_LENGTH, nullable = false)
    private String code;

    @Column(name = "IMAGE_URL", length = STICKER_IMAGE_MAX_LENGTH, nullable = false)
    private String imageUrl;

    @Column(name = "ALT_TEXT", length = STICKER_ALT_TEXT_MAX_LENGTH)
    private String altText;

    @Column(name = "SORT_NUMBER", nullable = false)
    private int sortNumber;
}
