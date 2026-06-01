package kr.magicbox.release.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReleaseDomainEventType {

    RELEASE_CREATED("release-created"),
    RELEASE_DELETED("release-deleted"),
    RELEASE_UPDATED("release-updated"),
    STOCK_RESERVE_SUCCEEDED("stock-reserve-succeeded"),
    STOCK_RESERVE_FAILED("stock-reserve-failed");

    private final String value;
}
