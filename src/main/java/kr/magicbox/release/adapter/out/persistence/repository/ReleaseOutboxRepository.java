package kr.magicbox.release.adapter.out.persistence.repository;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseOutboxRepository extends JpaRepository<ReleaseOutboxEntity, Long> {
}
