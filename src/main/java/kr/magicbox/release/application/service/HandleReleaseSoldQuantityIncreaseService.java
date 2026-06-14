package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.HandleReleaseSoldQuantityIncreaseUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HandleReleaseSoldQuantityIncreaseService implements HandleReleaseSoldQuantityIncreaseUseCase {

    @Override
    public void handleReleaseSoldQuantityIncrease(Long releaseId) {
        log.info("[ReleaseSoldQuantityIncrease] sold_quantity 증가는 stock-reserve 사가에서 처리됩니다. releaseId={}", releaseId);
    }
}
