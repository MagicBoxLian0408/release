package kr.magicbox.release.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record StockReserveFailedEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("reason") String reason,
        @JsonProperty("occurred_at") Instant occurredAt
) implements ReleaseDomainEvent {

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public ReleaseDomainEventType eventType() {
        return ReleaseDomainEventType.STOCK_RESERVE_FAILED;
    }
}
