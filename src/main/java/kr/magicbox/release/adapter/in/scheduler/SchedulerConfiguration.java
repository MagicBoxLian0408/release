package kr.magicbox.release.adapter.in.scheduler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
public class SchedulerConfiguration {
}
