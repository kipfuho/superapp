package com.superapp.branch_service.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.BranchMenuPublish;
import com.superapp.branch_service.repo.BranchMenuPublishRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublishService {
    private final MenuQueryService query;
    private final BranchMenuPublishRepo pubRepo;

    public Map<String, Object> publish(String branchId, String notes, String createdBy) {
        Map<String, Object> payload = query.getBranchMenu(branchId); // uses cache; OK as snapshot source
        BranchMenuPublish latest = pubRepo.findFirstByBranchIdOrderByVersionDesc(branchId).orElse(null);
        int next = latest == null ? 1 : latest.getVersion() + 1;
        BranchMenuPublish doc = BranchMenuPublish.builder()
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
