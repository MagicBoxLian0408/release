package kr.magicbox.release.application.service;

import kr.magicbox.release.adapter.in.kafka.event.StockReserveCommandEvent;
import kr.magicbox.release.application.port.in.HandleStockReserveUseCase;
import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.event.StockReserveFailedEvent;
import kr.magicbox.release.domain.event.StockReserveSucceededEvent;
import kr.magicbox.release.domain.exception.ReleaseNotFoundException;
import kr.magicbox.release.domain.exception.ReleaseStatusConflictException;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleStockReserveService implements HandleStockReserveUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;
    private final ReleaseOutboxPort releaseOutboxPort;

    @Override
    @Transactional
    public void handleStockReserve(StockReserveCommandEvent event) {
        if (event.items() == null || event.items().isEmpty()) {
            log.warn("[StockReserve] items 없음. orderId={}", event.orderId());
            releaseOutboxPort.save(StockReserveFailedEvent.builder()
                    .orderId(event.orderId())
                    .customerId(event.customerId())
                    .reason("items 없음")
                    .occurredAt(Instant.now())
                    .build());
            return;
        }

        List<StockReserveSucceededEvent.ItemPayload> succeededItems = new ArrayList<>();
        for (StockReserveCommandEvent.ItemPayload item : event.items()) {
            Long releaseId = item.productId();
            try {
                Release release = releaseRepositoryPort.findById(ReleaseId.of(releaseId));
                release.increaseSoldQuantity();
                releaseRepositoryPort.update(release);
                succeededItems.add(StockReserveSucceededEvent.ItemPayload.builder()
                        .orderLineId(item.orderLineId())
                        .sellerId(release.getCreatorId().value())
                        .amount((long) item.quantity() * item.unitPrice())
                        .build());
            } catch (ReleaseNotFoundException | ReleaseStatusConflictException e) {
                log.warn("[StockReserve] 재고 예약 실패. orderId={}, releaseId={}, reason={}",
                        event.orderId(), releaseId, e.getMessage());
                releaseOutboxPort.save(StockReserveFailedEvent.builder()
                        .orderId(event.orderId())
                        .customerId(event.customerId())
                        .reason(e.getMessage())
                        .occurredAt(Instant.now())
                        .build());
                return;
            }
        }

        log.info("[StockReserve] 재고 예약 성공. orderId={}", event.orderId());
        releaseOutboxPort.save(StockReserveSucceededEvent.builder()
                .orderId(event.orderId())
                .customerId(event.customerId())
                .totalAmount(event.totalAmount())
                .items(succeededItems)
                .occurredAt(Instant.now())
                .build());
    }
}
