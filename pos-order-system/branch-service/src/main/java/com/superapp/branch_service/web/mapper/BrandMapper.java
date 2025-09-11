package com.superapp.branch_service.web.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.superapp.branch_service.domain.Brand;
import com.superapp.branch_service.web.dto.BrandDtos.CreateBrandReq;
import com.superapp.branch_service.web.dto.BrandDtos.UpdateBrandReq;

public interface BrandMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Brand toBrand(CreateBrandReq req);

    @Mapping(target = "code", ignore = true)
    void updateBrand(UpdateBrandReq req, @MappingTarget Brand target);
}
