package kr.magicbox.release.domain.event;

public interface ReleaseDomainEvent {
    String key();
    ReleaseDomainEventType eventType();
}
