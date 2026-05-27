package kr.magicbox.release.application.port.in;

import kr.magicbox.release.application.dto.command.UpdateReleaseCommand;

public interface UpdateReleaseUseCase {
    void updateRelease(UpdateReleaseCommand command);
}
