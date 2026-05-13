package ratelimiter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ratelimiter.service.RateLimiterService;
import ratelimiter.service.RedisRateLimiterService;

import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ApiController {

    // @GetMapping("/hello")
    // public String hello() {
    // return "Hello Ishaan! Your backend is working 🚀";
    // }

    // @GetMapping("/name")
    // public String name() {
    // return "My name is Ishaan";
    // }

    // private final RateLimiterService rateLimiterService;/
    private final RedisRateLimiterService rateLimiterService;
    private final StringRedisTemplate redisTemplate;

    public ApiController(RedisRateLimiterService rateLimiterService,
            StringRedisTemplate redisTemplate) {
        this.rateLimiterService = rateLimiterService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/api/test")
    public ResponseEntity<String> test(HttpServletRequest request) {

        String userId = request.getRemoteAddr();

        boolean allowed = rateLimiterService.isAllowed(userId);

        if (!allowed) {
            return ResponseEntity.status(429)
                    .header("X-Rate-Limit", "5")
                    .header("X-Rate-Limit-Window", "10s")
                    .body("Too many requests ❌");
        }

        return ResponseEntity.ok()
                .header("X-Rate-Limit", "5")
                .header("X-Rate-Limit-Remaining", "3")
                .body("Request successful ✅");
    }

    @GetMapping("/redis-test")
    public String redisTest() {
        redisTemplate.opsForValue().set("name", "Ishaan");
        return redisTemplate.opsForValue().get("name");
    }
}
