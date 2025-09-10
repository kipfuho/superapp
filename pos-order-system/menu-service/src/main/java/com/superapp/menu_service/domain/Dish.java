package com.superapp.menu_service.domain;

import lombok.*;
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
}
