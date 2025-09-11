package com.superapp.branch_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.Branch;
import com.superapp.branch_service.repo.BranchRepo;
import com.superapp.branch_service.web.dto.BranchDtos.CreateBranchReq;
import com.superapp.branch_service.web.dto.BranchDtos.UpdateBranchReq;
import com.superapp.branch_service.web.mapper.BranchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepo repo;
    private final BranchMapper mapper;

    public List<Branch> list() {
        return repo.findAll();
    }

    public List<Branch> listByBrandId(String brandId) {
        return repo.findByBrandId(brandId);
    }

    public Branch create(CreateBranchReq req) {
        Branch branch = mapper.toBranch(req);
        Branch saved = repo.save(branch);
        return saved;
    }

    public Branch update(String id, UpdateBranchReq req) {
        Branch b = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));
        mapper.updateBranch(req, b);
        return repo.save(b);
    }
}
