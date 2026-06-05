package kr.magicbox.release.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.release.domain.enums.ReleaseLevel;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ReleaseCreatedEvent(
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("level") ReleaseLevel level,
        @JsonProperty("status") ReleaseStatus status,
        @JsonProperty("price") Long price,
        @JsonProperty("limited_quantity") Integer limitedQuantity,
        @JsonProperty("scheduled_at") Instant scheduledAt,
        @JsonProperty("media_urls") List<String> mediaUrls,
        @JsonProperty("occurred_at") Instant occurredAt
) implements ReleaseDomainEvent {

    @Override
    public String key() {
        return releaseId.toString();
    }

    @Override
    public ReleaseDomainEventType eventType() {
        return ReleaseDomainEventType.RELEASE_CREATED;
    }
}
