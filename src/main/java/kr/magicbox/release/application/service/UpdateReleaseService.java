package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.command.MediaCommand;
import kr.magicbox.release.application.dto.command.UpdateReleaseCommand;
import kr.magicbox.release.application.port.in.UpdateReleaseUseCase;
import kr.magicbox.release.application.port.out.CreatorIdQueryPort;
import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.event.ReleaseUpdatedEvent;
import kr.magicbox.release.domain.event.ReleaseUpdatedEvent.ReleaseSnapshot;
import kr.magicbox.release.domain.exception.ReleaseUnauthorizedException;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateReleaseService implements UpdateReleaseUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;
    private final CreatorIdQueryPort creatorIdQueryPort;
    private final ReleaseOutboxPort releaseOutboxPort;

    @Transactional
    @Override
    public void updateRelease(UpdateReleaseCommand command) {
        Release release = releaseRepositoryPort.findById(command.releaseId());

        CreatorId creatorId = creatorIdQueryPort.getCreatorId(command.userId());
        if (!release.getCreatorId().equals(creatorId)) {
            throw new ReleaseUnauthorizedException();
        }

        ReleaseSnapshot before = new ReleaseSnapshot(
                release.getTitle(),
                release.getDescription(),
                release.getMediaList().stream().map(ReleaseMedia::getMediaUrl).toList()
        );

        List<ReleaseMedia> mediaList = Optional.ofNullable(command.mediaList())
                .map(list -> list.stream().map(this::toReleaseMedia).toList())
                .orElse(null);

        release.update(command.title(), command.description(), command.price(), command.limitedQuantity(), mediaList);
        releaseRepositoryPort.update(release);

        ReleaseSnapshot after = new ReleaseSnapshot(
                release.getTitle(),
                release.getDescription(),
                release.getMediaList().stream().map(ReleaseMedia::getMediaUrl).toList()
        );

        if (!before.equals(after)) {
            releaseOutboxPort.save(ReleaseUpdatedEvent.builder()
                    .releaseId(release.getId().value())
                    .creatorId(creatorId.value())
                    .before(before)
                    .after(after)
                    .occurredAt(Instant.now())
                    .build());
        }
    }

    private ReleaseMedia toReleaseMedia(MediaCommand mediaCommand) {
        return ReleaseMedia.builder()
                .mediaUrl(mediaCommand.mediaUrl())
                .sortOrder(mediaCommand.sortOrder())
                .build();
    }
}
