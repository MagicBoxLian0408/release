package kr.magicbox.release.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ReleaseUpdatedEvent(
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("before") ReleaseSnapshot before,
        @JsonProperty("after") ReleaseSnapshot after,
        @JsonProperty("occurred_at") Instant occurredAt
) implements ReleaseDomainEvent {

    public record ReleaseSnapshot(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("media_urls") List<String> mediaUrls
    ) {}

    @Override
    public String key() {
        return releaseId.toString();
    }

    @Override
    public ReleaseDomainEventType eventType() {
        return ReleaseDomainEventType.RELEASE_UPDATED;
    }
}
