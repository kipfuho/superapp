package com.superapp.booking_service.web;

import java.time.Duration;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.booking_service.service.QueueService;
import com.superapp.booking_service.service.QueueService.QueueStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueController {
    private final QueueService service;

    @PostMapping("/{eventId}/admin/capacity")
    public void setCapacity(@PathVariable String eventId, @RequestParam long capacity) {
        service.setCapacity(eventId, capacity);
        service.setQueueSecret(eventId); // also set secret
    }

    @PostMapping("/{eventId}/admin/promote")
    public List<String> promote(@PathVariable String eventId,
            @RequestParam(defaultValue = "10") int toAdmit,
            @RequestParam(defaultValue = "300") long ttlSec) {
        return service.promote(eventId, toAdmit, Duration.ofSeconds(ttlSec), "slot:");
    }

    @PostMapping("/{eventId}/join")
    public ResponseEntity<?> join(@PathVariable String eventId, @RequestParam String userId) {
        long pos = service.join(eventId, userId);
        if (pos < 0)
            return ResponseEntity.status(403).body("blocked");
        return ResponseEntity.ok().body(java.util.Map.of("position", pos));
    }

    @GetMapping("/{eventId}/status")
    public ResponseEntity<?> status(@PathVariable String eventId, @RequestParam String userId) {
        QueueStatus status = service.getStatus(eventId, userId);
        return ResponseEntity.ok(status);
    }

    // claim to get token for using booking
    @PostMapping("/{eventId}/claim")
    public ResponseEntity<?> claim(@PathVariable String eventId,
            @RequestParam String userId,
            @RequestParam String token) {
        String ok = service.claim(eventId, userId, token);
        return ok != null ? ResponseEntity.ok().build()
                : ResponseEntity.status(409).body("invalid_or_expired");
    }

    @PostMapping("/{eventId}/leave")
    public void leave(@PathVariable String eventId, @RequestParam String userId) {
        service.leave(eventId, userId);
    }
}
