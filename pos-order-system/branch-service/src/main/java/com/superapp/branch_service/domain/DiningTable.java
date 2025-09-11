package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("dining_tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiningTable {
    @Id
    private String id;
    @Indexed
    private String branchId;
    @Indexed(unique = true)
    private String code; // unique per branch (consider compound unique)
    private String name; // optional label shown to staff
    private Integer capacity; // number of seats
    private String areaId; // ref to TableArea
    private Integer sort; // display order
    private TableStatus status; // AVAILABLE/OCCUPIED/RESERVED/MERGED/OUT_OF_SERVICE
    private String mergedIntoId; // if merged, this table points to the primary table id
    @Builder.Default
    private boolean active = true;

    public enum TableStatus {
        AVAILABLE, OCCUPIED, RESERVED, MERGED, OUT_OF_SERVICE
    }
}
