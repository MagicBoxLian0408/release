package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.command.RegisterReleaseCommand;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.CreatorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterReleaseTxService {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Transactional
    public Long save(CreatorId creatorId, RegisterReleaseCommand command) {
        Release release = Release.createBuilder()
                .creatorId(creatorId)
                .title(command.title())
                .description(command.description())
                .thumbnailUrl(command.thumbnailUrl())
                .level(command.level())
                .price(command.price())
                .limitedQuantity(command.limitedQuantity())
                .scheduledAt(command.scheduledAt())
                .build();
        return releaseRepositoryPort.save(release);
    }
}
