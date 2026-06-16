package kr.magicbox.release.adapter.out.persistence.repository;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseEntity;
import kr.magicbox.release.domain.enums.ReleaseStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReleaseJpaRepository extends JpaRepository<ReleaseEntity, Long> {

    @Query("SELECT r FROM ReleaseEntity r LEFT JOIN FETCH r.releaseMediaList WHERE r.id = :id")
    Optional<ReleaseEntity> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT r FROM ReleaseEntity r LEFT JOIN FETCH r.releaseMediaList WHERE r.creatorId = :creatorId")
    List<ReleaseEntity> findByCreatorId(@Param("creatorId") Long creatorId);

    long countByCreatorId(Long creatorId);

    List<ReleaseEntity> findByStatusAndScheduledAtBefore(ReleaseStatus status, Instant scheduledAt, Pageable pageable);

    @Query("SELECT DISTINCT r FROM ReleaseEntity r LEFT JOIN FETCH r.releaseMediaList WHERE r.id < :cursorId ORDER BY r.id DESC")
    List<ReleaseEntity> findByIdLessThanOrderByIdDesc(@Param("cursorId") Long cursorId, Pageable pageable);

    @Query("SELECT DISTINCT r FROM ReleaseEntity r LEFT JOIN FETCH r.releaseMediaList ORDER BY r.id DESC")
    List<ReleaseEntity> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT r.creatorId FROM ReleaseEntity r WHERE r.id = :id")
    Optional<Long> findCreatorIdById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ReleaseEntity r SET r.soldQuantity = r.soldQuantity + :quantity, " +
           "r.status = CASE WHEN r.soldQuantity + :quantity >= r.limitedQuantity THEN 'SOLD_OUT' ELSE r.status END " +
           "WHERE r.id = :id AND r.status = 'ON_SALE' AND r.soldQuantity + :quantity <= r.limitedQuantity")
    int increaseSoldQuantity(@Param("id") Long id, @Param("quantity") int quantity);
}
