package kr.magicbox.release.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "release_inbox")
public class ReleaseInboxEntity extends BaseEntity {

    @Column(name = "event_key", nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String topic;

    @Column(name = "kafka_partition", nullable = false)
    private Integer partition;

    @Column(name = "kafka_offset", nullable = false)
    private Long offset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReleaseInboxStatus status;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Builder
    public ReleaseInboxEntity(String key, String topic, Integer partition, Long offset,
                              ReleaseInboxStatus status, Instant occurredAt) {
        this.key = key;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.status = status;
        this.occurredAt = occurredAt;
    }

    public void markProcessed() {
        this.status = ReleaseInboxStatus.PROCESSED;
    }

    public void markDeadLettered() {
        this.status = ReleaseInboxStatus.DEAD_LETTERED;
    }
}
