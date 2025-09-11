package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("table_areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableArea {
    @Id
    private String id;
    @Indexed
    private String branchId;
    private String name;
    private int sort;
    @Builder.Default
    private boolean active = true;
}
