package kr.magicbox.release.adapter.out.persistence.repository;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReleaseInboxRepository extends JpaRepository<ReleaseInboxEntity, Long> {
    boolean existsByKey(String key);
    Optional<ReleaseInboxEntity> findByTopicAndPartitionAndOffset(String topic, Integer partition, Long offset);
}
