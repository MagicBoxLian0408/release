package kr.magicbox.release.application.port.in;

import kr.magicbox.release.application.dto.command.RegisterReleaseCommand;

public interface RegisterReleaseUseCase {
    Long registerRelease(RegisterReleaseCommand command);
}
