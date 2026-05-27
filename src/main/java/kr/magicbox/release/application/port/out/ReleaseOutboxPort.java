package kr.magicbox.release.application.port.out;

import kr.magicbox.release.domain.event.ReleaseDomainEvent;

public interface ReleaseOutboxPort {
    void save(ReleaseDomainEvent event);
}
