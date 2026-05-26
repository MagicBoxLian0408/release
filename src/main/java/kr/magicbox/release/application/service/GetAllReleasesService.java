package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.query.GetAllReleasesQuery;
import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.application.port.in.GetAllReleasesUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllReleasesService implements GetAllReleasesUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Transactional(readOnly = true)
    @Override
    public List<ReleaseResult> getAllReleases(GetAllReleasesQuery query) {
        return releaseRepositoryPort.findAllByCursor(query.cursorId(), query.size())
                .stream()
                .map(ReleaseResult::from)
                .toList();
    }
}
