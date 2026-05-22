local key = KEYS[1]
local currentTime = tonumber(ARGV[1])
local windowSize = tonumber(ARGV[2])
local maxRequests = tonumber(ARGV[3])

-- Remove old requests
redis.call('ZREMRANGEBYSCORE', key, 0, currentTime - windowSize)

-- Count current requests
local requestCount = redis.call('ZCARD', key)

if requestCount >= maxRequests then
    return 0
end

-- Add current request
redis.call('ZADD', key, currentTime, currentTime)

-- Set expiry
redis.call('EXPIRE', key, math.ceil(windowSize / 1000))

return 1
