package com.superapp.event_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.url}")
    private String publicBase;

    public Map<String, String> createEventImageUploadUrl(String eventId, String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image uploads are allowed (Content-Type must start with image/).");
        }

        final Set<String> ALLOWED = Set.of(
                "image/jpeg", // .jpg
                "image/png", // .png
                "image/webp" // .webp
        );
        if (!ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported image type: " + contentType);
        }

        final Map<String, String> EXT = Map.of(
                "image/jpeg", ".jpg",
                "image/png", ".png",
                "image/webp", ".webp");
        String ext = EXT.get(contentType);

        String key = "public/ticket-pro/images/event/" + eventId + "/" + UUID.randomUUID() + ext;

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType) // must match exactly on upload if present
                .build();

        PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putReq)
                .build();

        PresignedPutObjectRequest p = presigner.presignPutObject(presign);

        return Map.of(
                "uploadUrl", p.url().toString(),
                "key", key,
                "publicUrl", publicBase + "/" + key);
    }
}
