package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.command.DeleteReleaseCommand;
import kr.magicbox.release.application.port.in.DeleteReleaseUseCase;
import kr.magicbox.release.application.port.out.CreatorIdQueryPort;
import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.event.ReleaseDeletedEvent;
import kr.magicbox.release.domain.exception.ReleaseUnauthorizedException;
import kr.magicbox.release.domain.vo.CreatorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DeleteReleaseService implements DeleteReleaseUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;
    private final CreatorIdQueryPort creatorIdQueryPort;
    private final ReleaseOutboxPort releaseOutboxPort;

    @Transactional
    @Override
    public void deleteRelease(DeleteReleaseCommand command) {
        Release release = releaseRepositoryPort.findById(command.releaseId());

        CreatorId creatorId = creatorIdQueryPort.getCreatorId(command.userId());
        if (!release.getCreatorId().equals(creatorId)) {
            throw new ReleaseUnauthorizedException();
        }

        releaseRepositoryPort.delete(command.releaseId());

        releaseOutboxPort.save(ReleaseDeletedEvent.builder()
                .releaseId(release.getId().value())
                .creatorId(creatorId.value())
                .title(release.getTitle())
                .mediaUrls(release.getMediaList().stream()
                        .map(m -> m.getMediaUrl())
                        .toList())
                .occurredAt(Instant.now())
                .build());
    }
}
