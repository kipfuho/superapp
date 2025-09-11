package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tax_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRule {
    @Id
    private String id;
    private String name;
    private double ratePct; // store pct as number, compute amounts elsewhere
    private boolean inclusive;
}
