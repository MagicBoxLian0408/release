package kr.magicbox.release.application.dto.result;

import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.enums.MagicGenre;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Builder
public record ReleaseResult(
        Long releaseId,
        Long creatorId,
        String title,
        String description,
        List<ReleaseMediaResult> mediaList,
        ReleaseLevel level,
        ReleaseStatus status,
        Long price,
        Integer limitedQuantity,
        Integer soldQuantity,
        Set<MagicGenre> categories,
        Instant scheduledAt,
        Instant createdAt,
        Instant updatedAt
) {
    public boolean isOnSale() {
        return status == ReleaseStatus.ON_SALE;
    }

    public static ReleaseResult from(Release release) {
        List<ReleaseMediaResult> mediaList = release.getMediaList().stream()
                .map(ReleaseMediaResult::from)
                .toList();
        return ReleaseResult.builder()
                .releaseId(release.getId().value())
                .creatorId(release.getCreatorId().value())
                .title(release.getTitle())
                .description(release.getDescription())
                .mediaList(mediaList)
                .level(release.getLevel())
                .status(release.getStatus())
                .price(release.getPrice())
                .limitedQuantity(release.getLimitedQuantity())
                .soldQuantity(release.getSoldQuantity())
                .categories(release.getCategories())
                .scheduledAt(release.getScheduledAt())
                .createdAt(release.getCreatedAt())
                .updatedAt(release.getUpdatedAt())
                .build();
    }
}
