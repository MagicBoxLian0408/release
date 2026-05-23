package kr.magicbox.release.adapter.in.web.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.release.adapter.in.web.validation.ScheduledAtMultipleOfTenMinutes;
import kr.magicbox.release.application.dto.command.RegisterReleaseCommand;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.vo.UserId;

import java.time.Instant;

public record RegisterReleaseRequest(
        @NotBlank(message = "제목은 필수입니다.") String title,
        String description,
        String thumbnailUrl,
        ReleaseLevel level,
        @NotNull(message = "가격은 필수입니다.") @Min(value = 1, message = "가격은 1원 이상이어야 합니다.") Long price,
        @NotNull(message = "한정 수량은 필수입니다.") @Min(value = 1, message = "한정 수량은 1 이상이어야 합니다.") Integer limitedQuantity,
        @NotNull(message = "판매 예정 시각은 필수입니다.")
        @Future(message = "판매 예정 시각은 미래여야 합니다.")
        @ScheduledAtMultipleOfTenMinutes Instant scheduledAt
) {
    public RegisterReleaseCommand toCommand(UserId userId) {
        return RegisterReleaseCommand.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .level(level)
                .price(price)
                .limitedQuantity(limitedQuantity)
                .scheduledAt(scheduledAt)
                .build();
    }
}
