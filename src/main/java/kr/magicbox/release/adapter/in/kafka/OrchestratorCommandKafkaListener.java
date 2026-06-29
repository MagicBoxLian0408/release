package kr.magicbox.release.adapter.in.kafka;

import kr.magicbox.release.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.release.adapter.in.kafka.event.StockReserveCommandEvent;
import kr.magicbox.release.adapter.out.persistence.entity.ReleaseInboxEntity;
import kr.magicbox.release.adapter.out.persistence.repository.ReleaseInboxRepository;
import kr.magicbox.release.application.port.in.HandleStockReserveUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrchestratorCommandKafkaListener {

    private final HandleStockReserveUseCase handleStockReserveUseCase;
    private final ReleaseInboxRepository releaseInboxRepository;

    @Idempotent
    @RetryableTopic
    @KafkaListener(topics = "outbox.event.stock-reserve", groupId = "release-service")
    public void handleStockReserve(ConsumerRecord<String, StockReserveCommandEvent> consumerRecord) {
        log.info("[Inbox] stock-reserve 커맨드 수신. key={}", consumerRecord.key());
        handleStockReserveUseCase.handleStockReserve(consumerRecord.value());
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        releaseInboxRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(ReleaseInboxEntity::markDeadLettered);
    }
}
