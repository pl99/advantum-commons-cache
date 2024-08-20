package ru.advantum.commons.cache.runner;

public interface CacheRunner {

    ScheduleType getType();
    void shedule(Runnable task, String expression);
}
