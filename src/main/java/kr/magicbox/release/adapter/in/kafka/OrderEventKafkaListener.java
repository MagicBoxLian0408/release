package kr.magicbox.release.adapter.in.kafka;

import kr.magicbox.release.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.release.adapter.in.kafka.event.ReleaseSoldQuantityIncreaseEvent;
import kr.magicbox.release.application.port.in.HandleReleaseSoldQuantityIncreaseUseCase;
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
public class OrderEventKafkaListener {

    private final HandleReleaseSoldQuantityIncreaseUseCase handleReleaseSoldQuantityIncreaseUseCase;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.release-sold-quantity-increase", groupId = "release-service")
    public void handleReleaseSoldQuantityIncrease(
            ConsumerRecord<String, ReleaseSoldQuantityIncreaseEvent> consumerRecord) {
        log.info("[Inbox] release-sold-quantity-increase 이벤트 수신. key={}", consumerRecord.key());
        handleReleaseSoldQuantityIncreaseUseCase.handleReleaseSoldQuantityIncrease(
                consumerRecord.value().releaseId());
    }
}
