package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.HandleReleaseSoldQuantityIncreaseUseCase;
import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import kr.magicbox.release.domain.event.ReleaseStatusUpdatedEvent;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HandleReleaseSoldQuantityIncreaseService implements HandleReleaseSoldQuantityIncreaseUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;
    private final ReleaseOutboxPort releaseOutboxPort;

    @Transactional
    @Override
    public void handleReleaseSoldQuantityIncrease(Long releaseId) {
        Release release = releaseRepositoryPort.findById(ReleaseId.of(releaseId));
        ReleaseStatus beforeStatus = release.getStatus();
        boolean soldOut = release.increaseSoldQuantity();
        releaseRepositoryPort.update(release);

        if (soldOut) {
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
}
