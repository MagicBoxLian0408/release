package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoStartSaleChunkService {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Transactional
    public void startOne(Release release) {
        release.startSale();
        releaseRepositoryPort.update(release);
        log.info("[AutoStartSale] 판매 시작 처리. releaseId={}", release.getId().value());
    }
}
