package kr.magicbox.release.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ReleaseSoldQuantityIncreaseEvent(
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
