package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.command.RegisterReleaseCommand;
import kr.magicbox.release.application.port.in.RegisterReleaseUseCase;
import kr.magicbox.release.application.port.out.CreatorIdQueryPort;
import kr.magicbox.release.domain.vo.CreatorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterReleaseService implements RegisterReleaseUseCase {

    private final CreatorIdQueryPort creatorIdQueryPort;
    private final RegisterReleaseTxService registerReleaseTxService;

    @Override
    public Long registerRelease(RegisterReleaseCommand command) {
        CreatorId creatorId = creatorIdQueryPort.getCreatorId(command.userId()).join();
        return registerReleaseTxService.save(creatorId, command);
    }
}
