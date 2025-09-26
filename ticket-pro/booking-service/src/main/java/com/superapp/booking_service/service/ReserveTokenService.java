// src/main/java/.../ReserveTokenService.java
package com.superapp.booking_service.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReserveTokenService {

    private final StringRedisTemplate redis;
    private static final SecureRandom RNG = new SecureRandom();

    private static String keyFor(String hash) {
        return "resv:token:" + hash;
    }

    private static String reverseKey(UUID bookingId) {
        return "resv:booking:" + bookingId;
    }

    public String issue(UUID bookingId, String customerId, Duration ttl) {
        byte[] rnd = new byte[24];
        RNG.nextBytes(rnd);
        String token = "resv_" + Base64.getUrlEncoder().withoutPadding().encodeToString(rnd);

        String hash = hash(token);
        String key = keyFor(hash);

        Map<String, String> payload = Map.of(
                "bookingId", bookingId.toString(),
                "customerId", customerId);
        redis.opsForHash().putAll(key, payload);
        redis.expire(key, ttl);
        redis.opsForValue().set(reverseKey(bookingId), hash, ttl);
        return token;
    }

    public Optional<UUID> resolveBookingId(String token) {
        String hash = hash(token);
        Object val = redis.opsForHash().get(keyFor(hash), "bookingId");
        if (val == null)
            return Optional.empty();
        return Optional.of(UUID.fromString(val.toString()));
    }

    public void revokeByBookingId(UUID bookingId, String token) {
        String h = redis.opsForValue().get(reverseKey(bookingId));
        if (h == token) {
            redis.delete(keyFor(h));
            redis.delete(reverseKey(bookingId));
        }
        throw new IllegalStateException("Token not matched");
    }

    private static String hash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(dig.length * 2);
            for (byte b : dig)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Hashing failed", e);
        }
    }
}
