package kr.magicbox.release.adapter.out.persistence.repository;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseEntity;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReleaseJpaRepository extends JpaRepository<ReleaseEntity, Long> {

    Optional<ReleaseEntity> findById(Long id);

    List<ReleaseEntity> findByCreatorId(Long creatorId);

    long countByCreatorId(Long creatorId);

    List<ReleaseEntity> findByStatusAndScheduledAtBefore(ReleaseStatus status, Instant scheduledAt, Pageable pageable);

    List<ReleaseEntity> findByIdLessThanOrderByIdDesc(Long cursorId, Pageable pageable);

    List<ReleaseEntity> findAllByOrderByIdDesc(Pageable pageable);
}
