package kr.magicbox.release.application.port.in;

import kr.magicbox.release.application.dto.query.GetReleaseQuery;
import kr.magicbox.release.application.dto.result.ReleaseResult;

public interface GetReleaseUseCase {
    ReleaseResult getRelease(GetReleaseQuery query);
}
