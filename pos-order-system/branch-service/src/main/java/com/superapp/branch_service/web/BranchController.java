package com.superapp.branch_service.web;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.branch_service.domain.Branch;
import com.superapp.branch_service.service.BranchService;
import com.superapp.branch_service.web.dto.BranchDtos.CreateBranchReq;
import com.superapp.branch_service.web.dto.BranchDtos.UpdateBranchReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    // BRANCH
    @PostMapping("")
    public Branch createCat(@Valid @RequestBody CreateBranchReq req) {
        return branchService.create(req);
    }

    @PatchMapping("{id}")
    public Branch updateCat(@PathVariable String id, @Valid @RequestBody UpdateBranchReq req) {
        return branchService.update(id, req);
    }
}
