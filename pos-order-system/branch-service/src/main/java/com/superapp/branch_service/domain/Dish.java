package com.superapp.branch_service.domain;

import lombok.*;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {
    @Id
    private String id;
    @Indexed
    private String branchId;
    @Indexed
    private String categoryId;
    @Indexed(unique = true)
    private String sku;
    private String name;
    private long basePrice; // in minor units (VND) to avoid FP issues
    private String taxRuleId;
    @Builder.Default
    private boolean active = true;
    private java.util.List<String> tagIds;
    private int sort;
    private java.util.List<ImageAsset> images;
    // Link to groups with optional overrides
    private List<AppliedGroup> modifierGroups;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppliedGroup {
        private String groupId; // ref to ModifierGroup
        private Integer min; // optional per-dish override
        private Integer max; // optional per-dish override
        private boolean required; // optional per-dish override
        private Integer sort; // per-dish ordering
        private List<OptionOverride> optionOverrides; // e.g. per-dish price tweaks
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionOverride {
        private String optionId; // ref to ModifierOption
        private Long priceDeltaOverride; // optional: null = use group’s option price
    }

}
