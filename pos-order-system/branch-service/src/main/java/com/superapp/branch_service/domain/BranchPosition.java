package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("positions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchPosition {
    @Id
    private String id;
    @Indexed
    private String branchId; // optional: null if global
    private String name; // Waiter, Cashier, Chef, Manager
    private java.util.Set<String> permissions; // e.g., "ORDER_VOID", "PAYMENT_REFUND"
    @Builder.Default
    private boolean active = true;
}
