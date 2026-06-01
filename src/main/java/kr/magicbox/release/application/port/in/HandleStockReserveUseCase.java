package kr.magicbox.release.application.port.in;

import kr.magicbox.release.adapter.in.kafka.event.StockReserveCommandEvent;

public interface HandleStockReserveUseCase {
    void handleStockReserve(StockReserveCommandEvent event);
}
