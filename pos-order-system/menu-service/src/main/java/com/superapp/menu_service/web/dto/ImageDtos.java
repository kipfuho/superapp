package com.superapp.menu_service.web.dto;

import jakarta.validation.constraints.NotBlank;

public class ImageDtos {
    public record PresignDishImageReq(@NotBlank String contentType) {
    }

    public record AttachImageReq(@NotBlank String key, @NotBlank String url, String contentType, Integer width,
            Integer height) {
    }
}
