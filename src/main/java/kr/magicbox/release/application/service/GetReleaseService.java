package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.query.GetReleaseQuery;
import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.application.port.in.GetReleaseUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetReleaseService implements GetReleaseUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ReleaseResult getRelease(GetReleaseQuery query) {
        Release release = releaseRepositoryPort.findById(ReleaseId.of(query.releaseId()));
        return ReleaseResult.from(release);
    }
}
