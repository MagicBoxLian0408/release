package kr.magicbox.release.adapter.in.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kr.magicbox.release.adapter.in.web.validation.ScheduledAtMultipleOfTenMinutes;
import kr.magicbox.release.application.dto.command.MediaCommand;
import kr.magicbox.release.application.dto.command.RegisterReleaseCommand;
import kr.magicbox.release.domain.enums.MagicGenre;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.vo.UserId;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record RegisterReleaseRequest(
        @NotBlank(message = "제목은 필수입니다.") String title,
        String description,
        @NotEmpty(message = "미디어는 하나 이상 필수입니다.")
        List<@Valid @NotNull(message = "미디어 항목은 null일 수 없습니다.") MediaRequest> mediaList,
        ReleaseLevel level,
        @NotNull(message = "가격은 필수입니다.") @Min(value = 1, message = "가격은 1원 이상이어야 합니다.") Long price,
        @NotNull(message = "한정 수량은 필수입니다.") @Min(value = 1, message = "한정 수량은 1 이상이어야 합니다.") Integer limitedQuantity,
        @NotEmpty(message = "카테고리는 하나 이상 필수입니다.")
        Set<@NotNull(message = "카테고리 값은 null일 수 없습니다.") MagicGenre> categories,
        @NotNull(message = "판매 예정 시각은 필수입니다.")
        @Future(message = "판매 예정 시각은 미래여야 합니다.")
        @ScheduledAtMultipleOfTenMinutes Instant scheduledAt
) {
    public RegisterReleaseCommand toCommand(UserId userId) {
        List<MediaCommand> mediaCommands = mediaList.stream()
                .map(m -> new MediaCommand(m.mediaUrl(), m.sortOrder()))
                .toList();
        return RegisterReleaseCommand.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .mediaList(mediaCommands)
                .level(level)
                .price(price)
                .limitedQuantity(limitedQuantity)
                .categories(categories)
                .scheduledAt(scheduledAt)
                .build();
    }
}
