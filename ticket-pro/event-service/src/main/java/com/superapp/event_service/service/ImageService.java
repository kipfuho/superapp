package com.superapp.event_service.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ImageService {

    public Map<String, String> createEventImageUploadUrl(String eventId, String contentType);
}
