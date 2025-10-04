-- KEYS[1] = q:{eventId}:wait        (ZSET)
-- KEYS[2] = q:{eventId}:invited     (SET)
-- KEYS[3] = q:{eventId}:capacity    (STRING)
-- ARGV[1] = toAdmit (number)
-- ARGV[2] = inviteTTLSeconds (number)
-- ARGV[3] = eventId (string)
-- ARGV[4] = tokenPrefix (string), e.g., "slot:"
local capStr = redis.call('GET', KEYS[3])
local cap = 0
if capStr then
    cap = tonumber(capStr) or 0
end

local k = tonumber(ARGV[1]) or 0
if k > cap then
    k = cap
end
if k <= 0 then
    return {}
end

local now = redis.call('TIME') -- {sec, usec}
local sec = now[1]
local usec = now[2]

local promoted = {}
for i = 1, k do
    local item = redis.call('ZPOPMIN', KEYS[1])
    if not item or #item == 0 then
        break
    end
    local userId = item[1]
    if userId then
        local slotKey = 'q:' .. ARGV[3] .. ':slot:' .. userId

        local secretKey = 'q:' .. ARGV[3] .. ':secret'
        local secret = redis.call('GET', secretKey)
        if not secret then
            secret = 'secret'
        end
        local nonce = redis.call('INCR', 'q:' .. ARGV[3] .. ':nonce') -- Monotonic nonce to ensure uniqueness
        local digest = redis.sha1hex(secret .. ':' .. userId .. ':' .. sec .. ':' .. usec .. ':' .. tostring(nonce)) -- H(secret || userId || sec || usec || nonce)
        local token = ARGV[4] .. digest

        redis.call('SETEX', slotKey, tonumber(ARGV[2]), token)
        redis.call('SADD', KEYS[2], userId)
        table.insert(promoted, userId)
    end
end

return promoted
