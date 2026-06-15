package kr.magicbox.release.application.service;

import kr.magicbox.release.application.dto.command.RegisterReleaseCommand;
import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.event.ReleaseCreatedEvent;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterReleaseTxService {

    private final ReleaseRepositoryPort releaseRepositoryPort;
    private final ReleaseOutboxPort releaseOutboxPort;

    @Transactional
    public Long save(CreatorId creatorId, RegisterReleaseCommand command) {
        List<ReleaseMedia> mediaList = command.mediaList().stream()
                .map(m -> ReleaseMedia.builder()
                        .mediaUrl(m.mediaUrl())
                        .sortOrder(m.sortOrder())
                        .build())
                .toList();
        Release release = Release.createBuilder()
                .creatorId(creatorId)
                .title(command.title())
                .description(command.description())
                .mediaList(mediaList)
                .level(command.level())
                .price(command.price())
                .limitedQuantity(command.limitedQuantity())
                .categories(command.categories())
                .scheduledAt(command.scheduledAt())
                .build();
        Long releaseId = releaseRepositoryPort.save(release);

        List<String> mediaUrls = mediaList.stream()
                .map(ReleaseMedia::getMediaUrl)
                .toList();

        releaseOutboxPort.save(ReleaseCreatedEvent.builder()
                .releaseId(releaseId)
                .creatorId(creatorId.value())
                .title(command.title())
                .description(command.description())
                .level(command.level())
                .status(release.getStatus())
                .price(command.price())
                .limitedQuantity(command.limitedQuantity())
                .scheduledAt(command.scheduledAt())
                .mediaUrls(mediaUrls)
                .occurredAt(Instant.now())
                .build());

        return releaseId;
    }
}
