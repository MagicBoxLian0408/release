package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import kr.magicbox.release.domain.event.ReleaseStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoStartSaleChunkService {

    private final ReleaseRepositoryPort releaseRepositoryPort;
    private final ReleaseOutboxPort releaseOutboxPort;

    @Transactional
    public void startOne(Release release) {
        ReleaseStatus beforeStatus = release.getStatus();
        release.startSale();
        releaseRepositoryPort.update(release);
        log.info("[AutoStartSale] 판매 시작 처리. releaseId={}", release.getId().value());

        releaseOutboxPort.save(ReleaseStatusUpdatedEvent.builder()
                .releaseId(release.getId().value())
                .creatorId(release.getCreatorId().value())
                .beforeStatus(beforeStatus)
                .afterStatus(release.getStatus())
                .soldQuantity(release.getSoldQuantity())
                .limitedQuantity(release.getLimitedQuantity())
                .occurredAt(Instant.now())
                .build());
    }
}
