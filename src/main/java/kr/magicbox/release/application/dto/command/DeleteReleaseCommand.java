package kr.magicbox.release.application.dto.command;

import kr.magicbox.release.domain.vo.ReleaseId;
import kr.magicbox.release.domain.vo.UserId;

public record DeleteReleaseCommand(
        ReleaseId releaseId,
        UserId userId
) {
    public static DeleteReleaseCommand of(ReleaseId releaseId, UserId userId) {
        return new DeleteReleaseCommand(releaseId, userId);
    }
}
