package kr.magicbox.release.adapter.in.scheduler;

import kr.magicbox.release.application.port.in.AutoStartSaleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartSaleScheduler {

    private final AutoStartSaleUseCase autoStartSaleUseCase;
    private final RedissonClient redissonClient;
    private final SchedulerProperties schedulerProperties;

    @Scheduled(cron = "0 */10 * * * *")
    public void autoStartScheduledReleases() {
        RLock lock = redissonClient.getLock(schedulerProperties.getAutoStartSaleLockKey());
        if (!lock.tryLock()) {
            return;
        }
        log.info("[Scheduler] 판매 예정 릴리즈 자동 오픈 시작");
        autoStartSaleUseCase.autoStartScheduledReleases();
        lock.unlock();
        log.info("[Scheduler] 판매 예정 릴리즈 자동 오픈 완료");
    }
}
