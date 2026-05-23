package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.EndSaleUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EndSaleService implements EndSaleUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Override
    @Transactional
    public void endSale(ReleaseId releaseId) {
        Release release = releaseRepositoryPort.findById(releaseId);
        release.endSale();
        releaseRepositoryPort.update(release);
    }
}
