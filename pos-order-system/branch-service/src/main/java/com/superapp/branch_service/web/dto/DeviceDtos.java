package com.superapp.branch_service.web.dto;

import com.superapp.branch_service.domain.Device.DeviceType;

import jakarta.validation.constraints.*;

public class DeviceDtos {
    public record CreateDeviceReq(@NotBlank String branchId, @NotBlank String name, @NotNull DeviceType type,
            String ip, String mac, String printerModel, String station) {
    }

    public record UpdateDeviceReq(@NotBlank String id, String name, DeviceType type,
            String ip, String mac, String printerModel, String station, Boolean active, java.time.Instant lastSeenAt) {
    }
}
