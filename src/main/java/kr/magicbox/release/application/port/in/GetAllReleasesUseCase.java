package kr.magicbox.release.application.port.in;

import kr.magicbox.release.application.dto.query.GetAllReleasesQuery;
import kr.magicbox.release.application.dto.result.ReleaseResult;

import java.util.List;

public interface GetAllReleasesUseCase {
    List<ReleaseResult> getAllReleases(GetAllReleasesQuery query);
}
