package com.superapp.branch_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    private String id;
    @Indexed
    private String branchId;
    private String name; // e.g., POS-Front, KDS-Hotline, Printer-Kitchen
    private DeviceType type; // POS, KDS, PRINTER
    private String ip; // last known IP
    private String mac; // optional
    private String printerModel; // for PRINTER type
    private String station; // e.g., "Hot Line" for KDS/Printer routing
    private java.time.Instant lastSeenAt;
    @Builder.Default
    private boolean active = true;

    public enum DeviceType {
        POS, KDS, PRINTER
    }
}
