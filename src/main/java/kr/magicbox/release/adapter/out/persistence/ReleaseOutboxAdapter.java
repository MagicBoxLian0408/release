package kr.magicbox.release.adapter.out.persistence;

import kr.magicbox.release.adapter.out.persistence.entity.ReleaseOutboxEntity;
import kr.magicbox.release.adapter.out.persistence.repository.ReleaseOutboxRepository;
import kr.magicbox.release.application.port.out.ReleaseOutboxPort;
import kr.magicbox.release.domain.event.OrderIdAware;
import kr.magicbox.release.domain.event.ReleaseDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class ReleaseOutboxAdapter implements ReleaseOutboxPort {

    private final ReleaseOutboxRepository releaseOutboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(ReleaseDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        Long orderId = event instanceof OrderIdAware e ? e.orderId() : null;
        releaseOutboxRepository.save(ReleaseOutboxEntity.builder()
                .eventType(event.eventType().getValue())
                .aggregateKey(event.key())
                .payload(payload)
                .orderId(orderId)
                .build());
    }
}
