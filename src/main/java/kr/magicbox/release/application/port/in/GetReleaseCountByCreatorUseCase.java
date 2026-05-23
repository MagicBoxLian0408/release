package kr.magicbox.release.application.port.in;

import kr.magicbox.release.domain.vo.CreatorId;

public interface GetReleaseCountByCreatorUseCase {
    long getReleaseCount(CreatorId creatorId);
}
