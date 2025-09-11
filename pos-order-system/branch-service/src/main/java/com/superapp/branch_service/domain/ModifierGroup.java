package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("modifier_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifierGroup {
    @Id
    private String id;
    @Indexed
    private String branchId;
    private String name;
    private int min;
    private int max;
    private boolean required;
    private int sort;
    @Builder.Default
    private boolean active = true;
}
