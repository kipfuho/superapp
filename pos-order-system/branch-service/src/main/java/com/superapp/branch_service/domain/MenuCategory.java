package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("menu_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategory {
    @Id
    private String id;
    @Indexed
    private String branchId;
    private String name;
    @Indexed
    @Builder.Default
    private boolean active = true;
    private int sort;
}