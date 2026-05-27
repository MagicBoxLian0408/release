package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.HandleReleaseSoldQuantityIncreaseUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandleReleaseSoldQuantityIncreaseService implements HandleReleaseSoldQuantityIncreaseUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Transactional
    @Override
    public void handleReleaseSoldQuantityIncrease(Long releaseId) {
        Release release = releaseRepositoryPort.findById(ReleaseId.of(releaseId));
        release.increaseSoldQuantity();
        releaseRepositoryPort.update(release);
    }
}
