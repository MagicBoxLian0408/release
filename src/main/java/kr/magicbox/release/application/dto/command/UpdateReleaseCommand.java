package kr.magicbox.release.application.dto.command;

import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.vo.ReleaseId;
import kr.magicbox.release.domain.vo.UserId;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateReleaseCommand(
        ReleaseId releaseId,
        UserId userId,
        String title,
        String description,
        Long price,
        Integer limitedQuantity,
        ReleaseLevel level,
        List<MediaCommand> mediaList
) {}
