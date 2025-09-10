package com.superapp.menu_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superapp.menu_service.domain.Dish;
import com.superapp.menu_service.domain.ImageAsset;
import com.superapp.menu_service.repo.DishRepo;

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
    private final DishRepo dishRepo;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.url}")
    private String publicBase;

    public Map<String, String> createDishUploadUrl(String dishId, String contentType) {
        // 1) Validate content type (images only)
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image uploads are allowed (Content-Type must start with image/).");
        }

        // 2) Whitelist allowed image types (adjust as needed)
        final Set<String> ALLOWED = Set.of(
                "image/jpeg", // .jpg
                "image/png", // .png
                "image/webp" // .webp
        // add "image/avif", "image/gif" if you want
        );
        if (!ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported image type: " + contentType);
        }

        // 3) Map content-type -> extension
        final Map<String, String> EXT = Map.of(
                "image/jpeg", ".jpg",
                "image/png", ".png",
                "image/webp", ".webp");
        String ext = EXT.get(contentType);

        // 4) Build key (UUID makes objects immutable -> safe to set long cache headers)
        String key = "public/pos-order-system/images/dishes/" + dishId + "/" + UUID.randomUUID() + ext;

        // 5) Build the signed PutObjectRequest (no ACL; bucket owner enforced)
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType) // must match exactly on upload if present
                // .cacheControl("public, max-age=31536000, immutable") // optional but great for CDN
                // .contentDisposition("inline") // helps browsers render
                .build();

        PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putReq)
                .build();

        PresignedPutObjectRequest p = presigner.presignPutObject(presign);

        return Map.of(
                "uploadUrl", p.url().toString(),
                "key", key,
                // Use your CloudFront domain or viewer URL here if you serve via CDN
                "publicUrl", publicBase + "/" + key);
    }

    public Dish attachImageToDish(String dishId, ImageAsset asset) {
        Dish d = dishRepo.findById(dishId).orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        var imgs = d.getImages() == null ? new java.util.ArrayList<ImageAsset>()
                : new java.util.ArrayList<>(d.getImages());
        imgs.add(asset);
        d.setImages(imgs);
        return dishRepo.save(d);
    }
}
