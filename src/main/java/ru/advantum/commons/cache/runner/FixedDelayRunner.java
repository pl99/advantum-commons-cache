package ru.advantum.commons.cache.runner;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FixedDelayRunner implements CacheRunner {

    ThreadPoolTaskScheduler taskScheduler;

    @Override
    public ScheduleType getType() {
        return ScheduleType.FIXED_DELAY;
    }

    @Override
    public void shedule(Runnable task, String expression) {
        long millis = Duration.parse(expression).toMillis();
        taskScheduler.scheduleWithFixedDelay(task, millis);
    }

}
