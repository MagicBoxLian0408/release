package kr.magicbox.release.application.port.in;

import kr.magicbox.release.application.dto.result.ReleaseResult;
import kr.magicbox.release.domain.vo.CreatorId;

import java.util.List;

public interface GetReleaseListByCreatorUseCase {
    List<ReleaseResult> getReleaseListByCreator(CreatorId creatorId);
}
