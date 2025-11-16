package com.kicc.app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig(){
        return Caffeine.newBuilder().maximumSize(500).expireAfterWrite(10, TimeUnit.MINUTES);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        log.info("cacheManager >>> {}",cacheManager);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
