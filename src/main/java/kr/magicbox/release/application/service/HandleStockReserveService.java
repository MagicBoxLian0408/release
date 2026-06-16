package kr.magicbox.release.application.service;

import kr.magicbox.release.adapter.in.kafka.event.StockReserveCommandEvent;
import kr.magicbox.release.application.port.in.HandleStockReserveUseCase;
import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.event.StockReserveFailedEvent;
import kr.magicbox.release.domain.event.StockReserveSucceededEvent;
import kr.magicbox.release.domain.exception.ReleaseNotFoundException;
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
            int updated = releaseRepositoryPort.increaseSoldQuantity(releaseId, item.quantity());
            if (updated == 0) {
                log.warn("[StockReserve] 재고 예약 실패. orderId={}, releaseId={}", event.orderId(), releaseId);
                releaseOutboxPort.save(StockReserveFailedEvent.builder()
                        .orderId(event.orderId())
                        .customerId(event.customerId())
                        .reason("재고 부족 또는 판매 중 상태 아님")
                        .occurredAt(Instant.now())
                        .build());
                return;
            }
            Long sellerId = releaseRepositoryPort.findCreatorIdById(releaseId);
            succeededItems.add(StockReserveSucceededEvent.ItemPayload.builder()
                    .orderLineId(item.orderLineId())
                    .sellerId(sellerId)
                    .amount((long) item.quantity() * item.unitPrice())
                    .build());
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
