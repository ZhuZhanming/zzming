package top.zzming.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存的配置类
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Autowired
    private RedisTemplate<?, ?> redisTemplate;

    /**
     *  注解@Cache的管理器，设置过期时间的单位是秒
     */
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        Map<String, Long> expires = new HashMap<>();
        expires.put("user", 6000L);
        expires.put("file", 60*60L);
        cacheManager.setExpires(expires);
        cacheManager.setDefaultExpiration(10 * 60);
        return cacheManager;
    }

//    /**
//     *  注解@Cache key生成规则
//     *  所有参数字符串连起来
//     */
//    @Override
//    public KeyGenerator keyGenerator() {
//        return (target, method, params) -> {
//            StringBuilder builder = new StringBuilder();
//            for (Object param : params) {
//                builder.append(param.toString());
//            }
//            return builder.toString();
//        };
//    }

    /**
     * redis数据操作异常处理 这里的处理：在日志中打印出错误信息，但是放行 保证redis服务器出现连接等问题的时候不影响程序的正常运行，使得出问题时不用缓存
     * 
     * @return
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                LOGGER.error("CachePut异常：key=[{}]", key, exception);
            }

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.error("CacheGet异常：key=[{}]", key, exception);

            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                LOGGER.error("CacheEvict异常：key=[{}]", key, exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                LOGGER.error("CacheClear异常", exception);
            }
        };
    }

}
