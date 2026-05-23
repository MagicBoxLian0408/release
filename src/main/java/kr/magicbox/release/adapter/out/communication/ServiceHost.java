package kr.magicbox.release.adapter.out.communication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceHost {
    CREATOR("creator-service");

    private final String hostName;
}
