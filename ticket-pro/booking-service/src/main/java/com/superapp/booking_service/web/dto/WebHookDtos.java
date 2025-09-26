package com.superapp.booking_service.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class WebHookDtos {
    public record PaymentCallBackReq(@NotNull @NotBlank String partnerId, @NotNull @NotBlank String id,
            @NotNull Instant date,
            @NotNull @PositiveOrZero BigDecimal amount, String remark, @NotNull @NotBlank String checksum) {
        public Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            map.put("partnerId", this.partnerId);
            map.put("id", this.id);
            map.put("date", this.date.toString()); // ISO-8601
            map.put("amount", this.amount.toString());
            if (this.remark != null) {
                map.put("remark", this.remark);
            }
            map.put("checksum", this.checksum);
            return map;
        }
    }
}
