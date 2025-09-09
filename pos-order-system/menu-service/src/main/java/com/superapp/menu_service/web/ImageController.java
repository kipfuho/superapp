package com.superapp.menu_service.web;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.menu_service.domain.ImageAsset;
import com.superapp.menu_service.service.ImageService;
import com.superapp.menu_service.web.dto.ImageDtos.AttachImageReq;
import com.superapp.menu_service.web.dto.ImageDtos.PresignDishImageReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/dishes/{dishId}/presign")
    public Map<String, String> presign(@PathVariable String dishId, @Valid @RequestBody PresignDishImageReq req) {
        return imageService.createDishUploadUrl(dishId, req.contentType());
    }

    @PostMapping("/dishes/{dishId}/attach")
    public Object attach(@PathVariable String dishId, @Valid @RequestBody AttachImageReq req) {
        var asset = ImageAsset.builder().key(req.key()).url(req.url()).contentType(req.contentType())
                .width(req.width() == null ? 0 : req.width()).height(req.height() == null ? 0 : req.height()).build();
        return imageService.attachImageToDish(dishId, asset);
    }
}
