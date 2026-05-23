package kr.magicbox.release.application.dto.result;

import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReleaseResult(
        Long releaseId,
        Long creatorId,
        String title,
        String description,
        String thumbnailUrl,
        ReleaseLevel level,
        ReleaseStatus status,
        Long price,
        Integer limitedQuantity,
        Integer soldQuantity,
        Instant scheduledAt,
        Instant createdAt,
        Instant updatedAt
) {
    public boolean isOnSale() {
        return status == ReleaseStatus.ON_SALE;
    }

    public static ReleaseResult from(Release release) {
        return ReleaseResult.builder()
                .releaseId(release.getId().value())
                .creatorId(release.getCreatorId().value())
                .title(release.getTitle())
                .description(release.getDescription())
                .thumbnailUrl(release.getThumbnailUrl())
                .level(release.getLevel())
                .status(release.getStatus())
                .price(release.getPrice())
                .limitedQuantity(release.getLimitedQuantity())
                .soldQuantity(release.getSoldQuantity())
                .scheduledAt(release.getScheduledAt())
                .createdAt(release.getCreatedAt())
                .updatedAt(release.getUpdatedAt())
                .build();
    }
}
