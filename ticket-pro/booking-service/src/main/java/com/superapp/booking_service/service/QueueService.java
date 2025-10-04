package com.superapp.booking_service.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final StringRedisTemplate redis;

    private final RedisScript<Long> joinScript;
    private final RedisScript<List> promoteScript;
    private final RedisScript<List> claimScript;

    public enum QueueState {
        WAITING, INVITED, NOT_IN_QUEUE
    }

    public record QueueStatus(QueueState state, Long position, String token) {
    }

    // --- Key helpers ---
    private String waitKey(String eventId) {
        return "q:%s:wait".formatted(eventId);
    }

    private String invitedKey(String eventId) {
        return "q:%s:invited".formatted(eventId);
    }

    private String admittedKey(String eventId) {
        return "q:%s:admitted".formatted(eventId);
    }

    private String capacityKey(String eventId) {
        return "q:%s:capacity".formatted(eventId);
    }

    private String blockedKey(String eventId) {
        return "q:%s:blocked".formatted(eventId);
    }

    private String slotKey(String eventId, String userId) {
        return "q:%s:slot:%s".formatted(eventId, userId);
    }

    /** Join the queue; returns 0-based position or -1 if blocked. */
    public long join(String eventId, String userId) {
        Long pos = redis.execute(
                joinScript,
                List.of(waitKey(eventId), blockedKey(eventId)),
                userId, String.valueOf(System.currentTimeMillis()));
        return pos == null ? -1 : pos.longValue();
    }

    /**
     * Get current status, return position (0-based) or invite token if promoted.
     * Or -1 if not in queue
     */
    public QueueStatus getStatus(String eventId, String userId) {
        String token = getInviteToken(eventId, userId);
        if (token != null) {
            return new QueueStatus(QueueState.INVITED, null, token);
        }
        Long rank = redis.opsForZSet().rank(waitKey(eventId), userId);
        if (rank == null) {
            return new QueueStatus(QueueState.NOT_IN_QUEUE, null, null);
        }
        return new QueueStatus(QueueState.WAITING, rank, null);
    }

    /** Set capacity (how many users we allow concurrently). */
    public void setCapacity(String eventId, long capacity) {
        redis.opsForValue().set(capacityKey(eventId), String.valueOf(capacity));
    }

    /** Set secret (invite token secret). */
    public void setQueueSecret(String eventId) {
        String key = "q:%s:secret".formatted(eventId);
        if (Boolean.FALSE.equals(redis.hasKey(key))) {
            byte[] buf = new byte[32]; // 256-bit secret
            new SecureRandom().nextBytes(buf);
            String b64 = Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
            redis.opsForValue().set(key, b64);
        }
    }

    /** Promote up to `toAdmit` users; returns list of userIds promoted. */
    @SuppressWarnings("unchecked")
    public List<String> promote(String eventId, int toAdmit, Duration inviteTtl, String tokenPrefix) {
        List<?> res = redis.execute(
                promoteScript,
                List.of(waitKey(eventId), invitedKey(eventId), capacityKey(eventId)),
                String.valueOf(toAdmit),
                String.valueOf(inviteTtl.toSeconds()),
                eventId,
                tokenPrefix == null ? "slot:" : tokenPrefix);
        return (List<String>) (List<?>) (res == null ? List.of() : res);
    }

    /** Check if user currently has a valid invite (slot token exists). */
    public String getInviteToken(String eventId, String userId) {
        if (!Boolean.TRUE.equals(redis.opsForSet().isMember(invitedKey(eventId), userId))) {
            return null;
        }

        String token = redis.opsForValue().get(slotKey(eventId, userId));
        return token;
    }

    /**
     * Claim an invite by presenting the token; returns token (for checkout) if ok,
     * else null.
     */
    public String claim(String eventId, String userId, String providedToken) {
        List<?> res = redis.execute(
                claimScript,
                List.of(invitedKey(eventId), admittedKey(eventId), slotKey(eventId, userId)),
                userId, providedToken);
        if (res == null || res.isEmpty())
            return null;
        if (Objects.equals("ok", String.valueOf(res.get(0)))) {
            // Return a short-lived session token to proceed (you can issue a JWT here)
            return providedToken; // or mint a new JWT derived from it
        }
        return null;
    }

    /** Optional: leave queue. */
    public void leave(String eventId, String userId) {
        redis.opsForZSet().remove(waitKey(eventId), userId);
        redis.opsForSet().remove(invitedKey(eventId), userId);
        redis.delete(slotKey(eventId, userId));
        // don't touch admitted set here
    }

    /** Optional: compute ETA (rough) given admitRate/sec. */
    public long etaSeconds(String eventId, String userId, double admitRatePerSec) {
        QueueStatus status = getStatus(eventId, userId);
        if (status.state == QueueState.INVITED || status.state == QueueState.NOT_IN_QUEUE || admitRatePerSec <= 0)
            return -1;
        return Math.round(status.position / admitRatePerSec);
    }
}
