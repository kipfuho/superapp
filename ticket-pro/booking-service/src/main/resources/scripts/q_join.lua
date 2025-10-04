-- KEYS[1] = q:{eventId}:wait        (ZSET)
-- KEYS[2] = q:{eventId}:blocked     (SET)  - optional, can be empty/missing
-- ARGV[1] = userId
-- ARGV[2] = nowMillis (as string)
-- If blocked, return -1
if KEYS[2] and redis.call('EXISTS', KEYS[2]) == 1 then
    if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
        return -1
    end
end

-- Add if not already present
local rank = redis.call('ZRANK', KEYS[1], ARGV[1])
if not rank then
    redis.call('ZADD', KEYS[1], ARGV[2], ARGV[1])
    rank = redis.call('ZCARD', KEYS[1]) - 1
end

return rank
