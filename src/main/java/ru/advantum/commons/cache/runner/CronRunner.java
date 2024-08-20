package ru.advantum.commons.cache.runner;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CronRunner implements CacheRunner {

    ThreadPoolTaskScheduler taskScheduler;

    @Override
    public ScheduleType getType() {
        return ScheduleType.CRON;
    }

    @Override
    public void shedule(Runnable task, String expression) {
        CronTrigger cronTrigger = new CronTrigger(expression);
        taskScheduler.schedule(task, cronTrigger);
    }

}
