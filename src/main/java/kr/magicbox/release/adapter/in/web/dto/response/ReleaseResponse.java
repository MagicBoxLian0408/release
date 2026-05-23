package kr.magicbox.release.adapter.in.web.dto.response;

import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReleaseResponse(
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
    public static ReleaseResponse from(ReleaseResult result) {
        return ReleaseResponse.builder()
                .releaseId(result.releaseId())
                .creatorId(result.creatorId())
                .title(result.title())
                .description(result.description())
                .thumbnailUrl(result.thumbnailUrl())
                .level(result.level())
                .status(result.status())
                .price(result.price())
                .limitedQuantity(result.limitedQuantity())
                .soldQuantity(result.soldQuantity())
                .scheduledAt(result.scheduledAt())
                .createdAt(result.createdAt())
                .updatedAt(result.updatedAt())
                .build();
    }
}
