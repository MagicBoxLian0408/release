package kr.magicbox.release.adapter.out.persistence.repository;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseInboxRepository extends JpaRepository<ReleaseInboxEntity, Long> {
    boolean existsByKey(String key);
}
