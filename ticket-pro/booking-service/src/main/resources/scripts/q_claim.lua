-- KEYS[1] = q:{eventId}:invited     (SET)
-- KEYS[2] = q:{eventId}:admitted    (SET)
-- KEYS[3] = q:{eventId}:slot:{userId} (STRING with TTL)
-- ARGV[1] = userId
-- ARGV[2] = providedToken
local tok = redis.call('GET', KEYS[3])
if not tok then
    return {'err', 'expired'}
end
if tok ~= ARGV[2] then
    return {'err', 'invalid'}
end

if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 0 then
    return {'err', 'not_invited'}
end

redis.call('SREM', KEYS[1], ARGV[1])
redis.call('SADD', KEYS[2], ARGV[1])
redis.call('DEL', KEYS[3])

return {'ok'}
