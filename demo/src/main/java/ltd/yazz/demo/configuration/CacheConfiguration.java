package ltd.yazz.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheConfiguration {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(factory), RedisCacheConfiguration.defaultCacheConfig());
    }
}
