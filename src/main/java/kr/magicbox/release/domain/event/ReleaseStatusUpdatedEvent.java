package kr.magicbox.release.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReleaseStatusUpdatedEvent(
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("before_status") ReleaseStatus beforeStatus,
        @JsonProperty("after_status") ReleaseStatus afterStatus,
        @JsonProperty("sold_quantity") Integer soldQuantity,
        @JsonProperty("limited_quantity") Integer limitedQuantity,
        @JsonProperty("occurred_at") Instant occurredAt
) implements ReleaseDomainEvent {

    @Override
    public String key() {
        return releaseId.toString();
    }

    @Override
    public ReleaseDomainEventType eventType() {
        return ReleaseDomainEventType.RELEASE_STATUS_UPDATED;
    }
}
