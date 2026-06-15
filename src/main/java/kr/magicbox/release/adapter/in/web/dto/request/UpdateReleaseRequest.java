package kr.magicbox.release.adapter.in.web.dto.request;

import jakarta.validation.Valid;
import kr.magicbox.release.application.dto.command.MediaCommand;
import kr.magicbox.release.application.dto.command.UpdateReleaseCommand;
import kr.magicbox.release.domain.vo.ReleaseId;
import kr.magicbox.release.domain.vo.UserId;

import java.util.List;

public record UpdateReleaseRequest(
        String title,
        String description,
        Long price,
        Integer limitedQuantity,
        List<@Valid MediaRequest> mediaList
) {
    public UpdateReleaseCommand toCommand(Long releaseId, UserId userId) {
        List<MediaCommand> mediaCommands = mediaList == null ? null : mediaList.stream()
                .map(m -> new MediaCommand(m.mediaUrl(), m.sortOrder()))
                .toList();
        return UpdateReleaseCommand.builder()
                .releaseId(ReleaseId.of(releaseId))
                .userId(userId)
                .title(title)
                .description(description)
                .price(price)
                .limitedQuantity(limitedQuantity)
                .mediaList(mediaCommands)
                .build();
    }
}
