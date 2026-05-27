package kr.magicbox.release.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "release_media")
public class ReleaseMediaEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_id", nullable = false)
    private ReleaseEntity release;

    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Builder
    public ReleaseMediaEntity(String mediaUrl, Integer sortOrder) {
        this.mediaUrl = mediaUrl;
        this.sortOrder = sortOrder;
    }

    public void assignRelease(ReleaseEntity release) {
        this.release = release;
    }
}
