package kr.magicbox.release.adapter.in.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerProperties {
    private final String autoStartSaleLockKey;
}
