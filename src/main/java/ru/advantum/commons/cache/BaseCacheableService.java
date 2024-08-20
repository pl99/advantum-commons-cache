package ru.advantum.commons.cache;

import ru.advantum.commons.cache.runner.ScheduleType;

import java.util.List;

public interface BaseCacheableService<T, ID> {
    void cacheAllEntities();
    List<T> getAllEntities();
    T warmCache (T entity);
    T getCachedEntityById(ID id);

    ScheduleType getType();
}
