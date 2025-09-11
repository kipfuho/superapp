package com.superapp.branch_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.superapp.branch_service.domain.Branch;
import com.superapp.branch_service.web.dto.BranchDtos.CreateBranchReq;
import com.superapp.branch_service.web.dto.BranchDtos.UpdateBranchReq;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch toBranch(CreateBranchReq req);

    void updateBranch(UpdateBranchReq req, @MappingTarget Branch target);
}
