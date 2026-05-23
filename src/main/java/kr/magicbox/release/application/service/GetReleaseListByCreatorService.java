package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.application.port.in.GetReleaseListByCreatorUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.vo.CreatorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetReleaseListByCreatorService implements GetReleaseListByCreatorUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResult> getReleaseListByCreator(CreatorId creatorId) {
        return releaseRepositoryPort.findByCreatorId(creatorId)
                .stream()
                .map(ReleaseResult::from)
                .toList();
    }
}
