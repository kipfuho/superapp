package com.superapp.event_service.web.dto;

import jakarta.validation.constraints.NotBlank;

public class ImageDtos {
    public record PresignEventImageReq(@NotBlank String contentType) {

    }
}
