package com.superapp.menu_service.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageAsset {
    private String key; // s3 object key: images/dishes/{dishId}/uuid.jpg
    private String url; // public or signed URL for display
    private String contentType;
    private int width; // optional
    private int height; // optional
}
