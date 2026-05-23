package kr.magicbox.release.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "releases")
public class ReleaseEntity extends BaseEntity {

    @Version
    private Long version;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private ReleaseLevel level;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReleaseStatus status;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "limited_quantity", nullable = false)
    private Integer limitedQuantity;

    @Column(name = "sold_quantity", nullable = false)
    private Integer soldQuantity;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Builder
    public ReleaseEntity(Long creatorId, String title, String description, String thumbnailUrl,
                         ReleaseLevel level, ReleaseStatus status, Long price,
                         Integer limitedQuantity, Integer soldQuantity, Instant scheduledAt) {
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.level = level;
        this.status = status;
        this.price = price;
        this.limitedQuantity = limitedQuantity;
        this.soldQuantity = soldQuantity;
        this.scheduledAt = scheduledAt;
    }

    public void update(ReleaseStatus status, Integer soldQuantity) {
        this.status = status;
        this.soldQuantity = soldQuantity;
    }
}
