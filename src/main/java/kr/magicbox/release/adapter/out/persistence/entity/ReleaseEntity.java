package kr.magicbox.release.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.release.domain.enums.MagicGenre;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(name = "release_category", joinColumns = @JoinColumn(name = "release_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Set<MagicGenre> categories;

    @OneToMany(mappedBy = "release", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReleaseMediaEntity> releaseMediaList = new ArrayList<>();

    @Builder
    public ReleaseEntity(Long creatorId, String title, String description,
                         ReleaseLevel level, ReleaseStatus status, Long price,
                         Integer limitedQuantity, Integer soldQuantity, Set<MagicGenre> categories, Instant scheduledAt) {
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.level = level;
        this.status = status;
        this.price = price;
        this.limitedQuantity = limitedQuantity;
        this.soldQuantity = soldQuantity;
        this.categories = categories;
        this.scheduledAt = scheduledAt;
    }

    public void addMedia(ReleaseMediaEntity media) {
        media.assignRelease(this);
        this.releaseMediaList.add(media);
    }

    public void update(ReleaseStatus status, Integer soldQuantity) {
        this.status = status;
        this.soldQuantity = soldQuantity;
    }

    public void updateContent(String title, String description, Long price, Integer limitedQuantity, ReleaseLevel level, Set<MagicGenre> categories) {
        this.title = title;
        this.description = description;
        if (price != null) this.price = price;
        if (limitedQuantity != null) this.limitedQuantity = limitedQuantity;
        if (level != null) this.level = level;
        if (categories != null && !categories.isEmpty()) this.categories = categories;
    }
}
