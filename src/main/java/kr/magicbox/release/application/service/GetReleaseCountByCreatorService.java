package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.GetReleaseCountByCreatorUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.vo.CreatorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetReleaseCountByCreatorService implements GetReleaseCountByCreatorUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public long getReleaseCount(CreatorId creatorId) {
        return releaseRepositoryPort.countByCreatorId(creatorId);
    }
}
