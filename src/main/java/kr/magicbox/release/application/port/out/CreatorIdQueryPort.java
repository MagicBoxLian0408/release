package kr.magicbox.release.application.port.out;

import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.UserId;

public interface CreatorIdQueryPort {
    CreatorId getCreatorId(UserId userId);
}
