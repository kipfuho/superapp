package com.superapp.branch_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.Brand;
import com.superapp.branch_service.repo.BrandRepo;
import com.superapp.branch_service.web.dto.BrandDtos.CreateBrandReq;
import com.superapp.branch_service.web.dto.BrandDtos.UpdateBrandReq;
import com.superapp.branch_service.web.mapper.BrandMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepo repo;
    private final BrandMapper mapper;

    public List<Brand> list() {
        return repo.findAll();
    }

    public Brand create(CreateBrandReq req) {
        Brand b = mapper.toBrand(req);

        if (repo.existsByCode(req.code())) {
            throw new IllegalArgumentException("Brand with code already exists");
        }
        return repo.save(b);
    }

    public Brand update(String id, UpdateBrandReq req) {
        Brand b = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Brand not found"));
        mapper.updateBrand(req, b);
        return repo.save(b);
    }
}
