package ratelimiter.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RedisLuaRateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> redisScript;

    private final long WINDOW_SIZE = 10 * 1000;
    private final int MAX_REQUESTS = 5;

    public RedisLuaRateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("scripts/rateLimiter.lua"));
        redisScript.setResultType(Long.class);
    }

    public boolean isAllowed(String userId) {

        String key = "rate_limit:" + userId;

        Long result = redisTemplate.execute(
                redisScript,
                Collections.singletonList(key),
                String.valueOf(System.currentTimeMillis()),
                String.valueOf(WINDOW_SIZE),
                String.valueOf(MAX_REQUESTS));

        return result != null && result == 1;
    }
}