package com.superapp.menu_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("branch_menu_publish")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchMenuPublish {
    @Id
    private String id;
    @Indexed
    private String branchId;
    @Indexed(unique = true)
    private String branchVersionKey; // branchId:version
    private int version;
    private Object payload; // Map<String,Object> serialized snapshot
    private String createdBy;
    private java.time.Instant createdAt;
    private String notes;
}
