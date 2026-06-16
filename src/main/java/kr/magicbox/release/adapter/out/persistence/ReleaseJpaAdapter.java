package kr.magicbox.release.adapter.out.persistence;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseEntity;
import kr.magicbox.release.adapter.out.persistence.mapper.ReleaseMapper;
import kr.magicbox.release.adapter.out.persistence.repository.ReleaseJpaRepository;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import kr.magicbox.release.domain.exception.ReleaseNotFoundException;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseId;
import kr.magicbox.release.domain.vo.ReleaseMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReleaseJpaAdapter implements ReleaseRepositoryPort {

    private final ReleaseJpaRepository releaseJpaRepository;
    private final ReleaseMapper releaseMapper;

    @Override
    public Long save(Release release) {
        ReleaseEntity entity = releaseJpaRepository.save(releaseMapper.toEntity(release));
        return entity.getId();
    }

    @Override
    public void update(Release release) {
        ReleaseEntity entity = releaseJpaRepository.findById(release.getId().value())
                .orElseThrow(ReleaseNotFoundException::new);
        entity.update(release.getStatus(), release.getSoldQuantity());
        entity.updateContent(release.getTitle(), release.getDescription());
        syncMediaList(entity, release.getMediaList());
        releaseJpaRepository.save(entity);
    }

    @Override
    public Release findById(ReleaseId id) {
        ReleaseEntity entity = releaseJpaRepository.findById(id.value())
                .orElseThrow(ReleaseNotFoundException::new);
        return releaseMapper.toDomain(entity);
    }

    @Override
    public List<Release> findByCreatorId(CreatorId creatorId) {
        return releaseJpaRepository.findByCreatorId(creatorId.value())
                .stream()
                .map(releaseMapper::toDomain)
                .toList();
    }

    @Override
    public long countByCreatorId(CreatorId creatorId) {
        return releaseJpaRepository.countByCreatorId(creatorId.value());
    }

    @Override
    public List<Release> findScheduledBefore(Instant scheduledAt, int limit) {
        return releaseJpaRepository.findByStatusAndScheduledAtBefore(ReleaseStatus.SCHEDULED, scheduledAt, PageRequest.of(0, limit))
                .stream()
                .map(releaseMapper::toDomain)
                .toList();
    }

    @Override
    public List<Release> findAllByCursor(Long cursorId, int size) {
        List<ReleaseEntity> entities = cursorId == null
                ? releaseJpaRepository.findAllByOrderByIdDesc(PageRequest.of(0, size))
                : releaseJpaRepository.findByIdLessThanOrderByIdDesc(cursorId, PageRequest.of(0, size));
        return entities.stream()
                .map(releaseMapper::toDomain)
                .toList();
    }

    @Override
    public Long findCreatorIdById(Long id) {
        return releaseJpaRepository.findCreatorIdById(id)
                .orElseThrow(ReleaseNotFoundException::new);
    }

    @Override
    public int increaseSoldQuantity(Long id, int quantity) {
        return releaseJpaRepository.increaseSoldQuantity(id, quantity);
    }

    private void syncMediaList(ReleaseEntity entity, List<ReleaseMedia> newMediaList) {
        Set<String> incomingKeys = newMediaList.stream()
                .map(m -> m.getMediaUrl() + "|" + m.getSortOrder())
                .collect(Collectors.toSet());

        entity.getReleaseMediaList().removeIf(existing ->
                !incomingKeys.contains(existing.getMediaUrl() + "|" + existing.getSortOrder()));

        Set<String> existingKeys = entity.getReleaseMediaList().stream()
                .map(e -> e.getMediaUrl() + "|" + e.getSortOrder())
                .collect(Collectors.toSet());

        newMediaList.stream()
                .filter(m -> !existingKeys.contains(m.getMediaUrl() + "|" + m.getSortOrder()))
                .map(releaseMapper::toMediaEntity)
                .forEach(entity::addMedia);
    }
}
