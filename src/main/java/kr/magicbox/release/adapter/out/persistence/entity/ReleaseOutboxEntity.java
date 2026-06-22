package kr.magicbox.release.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "release_outbox")
public class ReleaseOutboxEntity extends BaseEntity {

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String aggregateKey;

    @Column(nullable = false, columnDefinition = "JSON")
    private String payload;

    @Column(name = "order_id")
    private Long orderId;

    @Builder
    public ReleaseOutboxEntity(String eventType, String aggregateKey, String payload, Long orderId) {
        this.eventType = eventType;
        this.aggregateKey = aggregateKey;
        this.payload = payload;
        this.orderId = orderId;
    }
}
