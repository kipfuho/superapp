package com.superapp.menu_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superapp.menu_service.domain.Dish;
import com.superapp.menu_service.domain.ImageAsset;
import com.superapp.menu_service.repo.DishRepo;

import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
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
        String ext = contentType != null && contentType.contains("png") ? ".png" : ".jpg";
        String key = "pos-order-system/images/dishes/" + dishId + "/" + UUID.randomUUID() + ext;

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .acl(ObjectCannedACL.PUBLIC_READ) // or use private + CloudFront signed URLs
                .build();

        var presign = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putReq)
                .build();

        PresignedPutObjectRequest url = presigner.presignPutObject(presign);
        return Map.of(
                "uploadUrl", url.url().toString(),
                "key", key,
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
