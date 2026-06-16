package kr.magicbox.release.application.port.out;

import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.CreatorId;
import kr.magicbox.release.domain.vo.ReleaseId;

import java.time.Instant;
import java.util.List;

public interface ReleaseRepositoryPort {

    Long save(Release release);

    void update(Release release);

    Release findById(ReleaseId id);

    List<Release> findByCreatorId(CreatorId creatorId);

    long countByCreatorId(CreatorId creatorId);

    List<Release> findScheduledBefore(Instant scheduledAt, int limit);

    List<Release> findAllByCursor(Long cursorId, int size);

    Long findCreatorIdById(Long id);

    int increaseSoldQuantity(Long id, int quantity);
}
