package kr.magicbox.release.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ReleaseDeletedEvent(
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("title") String title,
        @JsonProperty("media_urls") List<String> mediaUrls,
        @JsonProperty("occurred_at") Instant occurredAt
) implements ReleaseDomainEvent {

    @Override
    public String key() {
        return releaseId.toString();
    }

    @Override
    public ReleaseDomainEventType eventType() {
        return ReleaseDomainEventType.RELEASE_DELETED;
    }
}
