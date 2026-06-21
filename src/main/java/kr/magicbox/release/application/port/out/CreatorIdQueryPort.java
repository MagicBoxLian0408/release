package kr.magicbox.release.application.port.out;

import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.UserId;

import java.util.concurrent.CompletableFuture;

public interface CreatorIdQueryPort {
    CompletableFuture<CreatorId> getCreatorId(UserId userId);
}
