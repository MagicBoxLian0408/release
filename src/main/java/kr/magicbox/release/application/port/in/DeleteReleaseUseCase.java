package kr.magicbox.release.application.port.in;

import kr.magicbox.release.application.dto.command.DeleteReleaseCommand;

public interface DeleteReleaseUseCase {
    void deleteRelease(DeleteReleaseCommand command);
}
