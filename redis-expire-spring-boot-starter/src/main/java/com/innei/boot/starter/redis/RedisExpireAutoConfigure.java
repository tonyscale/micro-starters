package com.innei.boot.starter.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by LIUMI969 on 2017/5/18.
 *
 */
@Configuration
@Slf4j
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Import(RedisKeyExpireScannerRegistrar.class)
public class RedisExpireAutoConfigure {


    @Autowired(required=false)
    private CacheErrorHandler cacheErrorHandler;

    @Autowired(required=false)
    private CacheResolver cacheResolver;




    @Bean
    public CacheManager redisCacheManager(RedisTemplate redisTemplate, RedisKeysExpire redisKeysExpire){

        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setDefaultExpiration(3600L); //默认一个小时
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setExpires(redisKeysExpire.getKeysExpireMap());

        log.info("Redis Expire keys prefix {}",redisKeysExpire.getKeysExpireMap());
        return redisCacheManager;
    }


    @Bean
    public CachingConfigurer cachingConfigurer(){
        return new CachingConfigurerSupport(){
            public CacheErrorHandler errorHandler() {
                if(null == cacheErrorHandler){
                    return new CacheErrorHandler() {
                        @Override
                        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {

                            log.error("handleCacheGetError cache [{}],key [{}]",cache,key,exception);
                            if(null != exception){
                                throw exception;
                            }

                        }

                        @Override
                        public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                            log.error("handleCachePutError cache [{}],key [{}]",cache,key,exception);
                            if(null != exception){
                                throw exception;
                            }
                        }

                        @Override
                        public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                            log.error("handleCacheEvictError cache [{}],key [{}]",cache,key,exception);
                            if(null != exception){
                                throw exception;
                            }
                        }

                        @Override
                        public void handleCacheClearError(RuntimeException exception, Cache cache) {
                            log.error("handleCacheClearError cache [{}]",cache,exception);
                            if(null != exception){
                                throw exception;
                            }
                        }
                    };
                }
                return cacheErrorHandler;
            }

            @Override
            public CacheResolver cacheResolver() {
                return cacheResolver;
            }


            @Override
            public KeyGenerator keyGenerator() {
                return new DefaultKeyGenerator();
            }
        };
    }


}
