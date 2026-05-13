package ratelimiter.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisRateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private final int MAX_REQUESTS = 5;
    private final long WINDOW_SIZE = 10; // seconds

    public RedisRateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {

        long now = System.currentTimeMillis();
        long windowStart = now - (WINDOW_SIZE * 1000);

        String key = "rate_limit:" + userId;

        // 1. Remove old requests
        redisTemplate.opsForZSet()
                .removeRangeByScore(key, 0, windowStart);

        // 2. Get current count
        Long count = redisTemplate.opsForZSet().zCard(key);

        if (count != null && count >= MAX_REQUESTS) {
            return false;
        }

        // 3. Add current request
        redisTemplate.opsForZSet()
                .add(key, String.valueOf(now), now);

        // 4. Set expiry (auto cleanup)
        redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SIZE));

        return true;
    }
}