package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.StartSaleUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StartSaleService implements StartSaleUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Override
    @Transactional
    public void startSale(ReleaseId releaseId) {
        Release release = releaseRepositoryPort.findById(releaseId);
        release.startSale();
        releaseRepositoryPort.update(release);
    }
}
