package kr.magicbox.release.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReleaseDomainEventType {

    RELEASE_CREATED("release-created"),
    RELEASE_DELETED("release-deleted");

    private final String value;
}
