package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.AutoStartSaleUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoStartSaleService implements AutoStartSaleUseCase {

    private static final int CHUNK_SIZE = 100;

    private final ReleaseRepositoryPort releaseRepositoryPort;
    private final AutoStartSaleChunkService autoStartSaleChunkService;

    @Transactional
    @Override
    public void autoStartScheduledReleases() {
        List<Release> chunk;
        do {
            chunk = releaseRepositoryPort.findScheduledBefore(Instant.now(), CHUNK_SIZE);
            chunk.forEach(autoStartSaleChunkService::startOne);
        } while (chunk.size() == CHUNK_SIZE);
    }
}
