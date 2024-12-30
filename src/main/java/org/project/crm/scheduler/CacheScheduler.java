package org.project.crm.scheduler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.project.crm.service.CacheService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheScheduler {
    private final CacheService cacheService;

    @Scheduled(fixedRate = 6000)
    @PostConstruct
    public void evictAllCachesAtIntervals() {
        cacheService.evictAllCacheValues();
    }
}
