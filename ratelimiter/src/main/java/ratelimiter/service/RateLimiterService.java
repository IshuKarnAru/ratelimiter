package ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<String, Deque<Long>> requestMap = new ConcurrentHashMap<>();

    private final int MAX_REQUESTS = 5;
    private final long WINDOW_SIZE = 10 * 1000; // 10 sec

    public boolean isAllowed(String userId) {

        long now = System.currentTimeMillis();
        long windowStart = now - WINDOW_SIZE;

        // Thread-safe initialization
        requestMap.putIfAbsent(userId, new ConcurrentLinkedDeque<>());

        Deque<Long> timestamps = requestMap.get(userId);

        synchronized (timestamps) {
            // Remove expired timestamps from front
            while (!timestamps.isEmpty() && timestamps.peekFirst() < windowStart) {
                timestamps.pollFirst();
            }

            if (timestamps.size() >= MAX_REQUESTS) {
                return false;
            }

            timestamps.addLast(now);
            return true;
        }
    }
}