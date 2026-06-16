package kr.magicbox.release.adapter.in.kafka;

import kr.magicbox.release.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.release.adapter.in.kafka.event.StockReserveCommandEvent;
import kr.magicbox.release.application.port.in.HandleStockReserveUseCase;
import kr.magicbox.release.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrchestratorCommandKafkaListener {

    private final HandleStockReserveUseCase handleStockReserveUseCase;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.stock-reserve-release", groupId = "release-service")
    public void handleStockReserve(ConsumerRecord<String, StockReserveCommandEvent> consumerRecord) {
        log.info("[Inbox] stock-reserve-release 커맨드 수신. key={}", consumerRecord.key());
        handleStockReserveUseCase.handleStockReserve(consumerRecord.value());
    }
}
