package com.superapp.menu_service.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.superapp.menu_service.domain.BranchMenuPublish;
import com.superapp.menu_service.repo.BranchMenuPublishRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublishService {
    private final MenuQueryService query;
    private final BranchMenuPublishRepo pubRepo;

    public Map<String, Object> publish(String branchId, String notes, String createdBy) {
        var payload = query.getBranchMenu(branchId); // uses cache; OK as snapshot source
        var latest = pubRepo.findFirstByBranchIdOrderByVersionDesc(branchId).orElse(null);
        int next = latest == null ? 1 : latest.getVersion() + 1;
        var doc = BranchMenuPublish.builder()
                .branchId(branchId)
                .branchVersionKey(branchId + ":" + next)
                .version(next)
                .payload(payload)
                .createdBy(createdBy == null ? "system" : createdBy)
                .createdAt(java.time.Instant.now())
                .notes(notes)
                .build();
        pubRepo.save(doc);
        return Map.of("version", next, "createdAt", doc.getCreatedAt());
    }
}
