package kr.magicbox.release.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record StockReserveSucceededEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("total_amount") Long totalAmount,
        @JsonProperty("items") List<ItemPayload> items,
        @JsonProperty("occurred_at") Instant occurredAt
) implements ReleaseDomainEvent {

    @Builder
    public record ItemPayload(
            @JsonProperty("order_line_id") Long orderLineId,
            @JsonProperty("seller_id") Long sellerId,
            @JsonProperty("amount") Long amount
    ) {}

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public ReleaseDomainEventType eventType() {
        return ReleaseDomainEventType.STOCK_RESERVE_SUCCEEDED;
    }
}
