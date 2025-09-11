package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("modifier_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifierOption {
    @Id
    private String id;
    @Indexed
    private String groupId;
    private String name;
    private long priceDelta; // minor units
    @Builder.Default
    private boolean active = true;
    private int sort;
}
