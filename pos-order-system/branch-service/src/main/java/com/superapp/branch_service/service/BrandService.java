package com.superapp.branch_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.Brand;
import com.superapp.branch_service.repo.BrandRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {
    private BrandRepo repo;

    public List<Brand> list() {
        return repo.findAll();
    }

    public Brand create(Brand brand) {
        return repo.save(brand);
    }

    public Brand update(String id, Brand brand) {
        Brand existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Brand not found"));
        existing.setName(brand.getName());
        existing.setCode(brand.getCode());
        return repo.save(existing);
    }
}
