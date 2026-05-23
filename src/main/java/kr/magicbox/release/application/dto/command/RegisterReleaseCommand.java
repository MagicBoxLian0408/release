package kr.magicbox.release.application.dto.command;

import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record RegisterReleaseCommand(
        UserId userId,
        String title,
        String description,
        String thumbnailUrl,
        ReleaseLevel level,
        Long price,
        Integer limitedQuantity,
        Instant scheduledAt
) {}
