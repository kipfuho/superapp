package com.superapp.event_service.web;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.event_service.service.ImageService;
import com.superapp.event_service.web.dto.ImageDtos.PresignEventImageReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/events/{eventId}/presign")
    public Map<String, String> presign(@PathVariable String eventId, @Valid @RequestBody PresignEventImageReq req) {
        return imageService.createEventImageUploadUrl(eventId, req.contentType());
    }
}
