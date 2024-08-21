package ru.advantum.commons.cache;

import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.advantum.commons.cache.runner.CacheRunner;
import ru.advantum.commons.cache.runner.ScheduleType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public abstract class AbstractJpaCache<T, ID, R extends JpaRepository<T, ID>> implements BaseCacheableService<T, ID> {

    protected final R repository;
    @Autowired
    @NonFinal
    protected AbstractJpaCache<T, ID, R> me;

    protected final Map<ScheduleType, CacheRunner> cacheRunnerMap;

    protected AbstractJpaCache(R repository, List<CacheRunner> cacheRunners) {
        this.repository = repository;
        this.cacheRunnerMap = cacheRunners.stream().collect(Collectors.toMap(CacheRunner::getType, Function.identity()));
    }

    /**
     * Абстрактный метод для получения значения задержки
     *
     * @return строковое представление продолжительности,
     * например
     * PT30m - 30 мин
     * PT4H - 4 часа
     * PT15s - 15 секунд
     */
    protected abstract String getExpression();
    protected abstract ScheduleType getScheduleType();

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void cacheAllEntities() {
        CacheRunner runner = cacheRunnerMap.get(getType());
        runner.shedule(new Runnable() {
            @Override
            public void run() {
                log.debug("Caching all entities {}", getClass().getSimpleName());
                List<T> entities = repository.findAll();
                entities.forEach(me::warmCache);
                afterCachedAll(entities);
            }
        }, getExpression());
    }

    protected T getEntityById(ID id) {
        return repository.findById(id).orElse(null);
    }

    protected T cacheEntity(T entity) {
        log.debug("Caching entity {}", entity);
        return entity;
    }


    @Override
    public List<T> getAllEntities() {
        return repository.findAll();
    }

    /**
     * Позволяет выполнить некоторые действия после заполнения кэша,
     * например, уведомить фронт об изменениях
     * @param data
     */
    protected void afterCachedAll(List<T> data) {

    }
}
