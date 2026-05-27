package kr.magicbox.release.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReleaseDomainEventType {

    RELEASE_CREATED("release-created"),
    RELEASE_DELETED("release-deleted"),
    RELEASE_STATUS_UPDATED("release-status-updated");

    private final String value;
}
