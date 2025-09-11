package com.superapp.branch_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.superapp.branch_service.domain.Branch;
import com.superapp.branch_service.domain.BranchPosition;
import com.superapp.branch_service.domain.Device;
import com.superapp.branch_service.domain.DiningTable;
import com.superapp.branch_service.domain.TableArea;
import com.superapp.branch_service.web.dto.BranchDtos.CreateBranchReq;
import com.superapp.branch_service.web.dto.BranchDtos.UpdateBranchReq;
import com.superapp.branch_service.web.dto.BranchPositionDtos.CreatePositionReq;
import com.superapp.branch_service.web.dto.BranchPositionDtos.UpdatePositionReq;
import com.superapp.branch_service.web.dto.DeviceDtos.CreateDeviceReq;
import com.superapp.branch_service.web.dto.DeviceDtos.UpdateDeviceReq;
import com.superapp.branch_service.web.dto.TableDtos.CreateAreaReq;
import com.superapp.branch_service.web.dto.TableDtos.CreateTableReq;
import com.superapp.branch_service.web.dto.TableDtos.UpdateAreaReq;
import com.superapp.branch_service.web.dto.TableDtos.UpdateTableReq;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Branch toBranch(CreateBranchReq req);

    @Mapping(target = "code", ignore = true)
    void updateBranch(UpdateBranchReq req, @MappingTarget Branch target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    TableArea toArea(CreateAreaReq req);

    @Mapping(target = "branchId", ignore = true)
    void updateArea(UpdateAreaReq req, @MappingTarget TableArea target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "mergedIntoId", ignore = true)
    @Mapping(target = "status", ignore = true)
    DiningTable toTable(CreateTableReq req);

    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "code", ignore = true)
    void updateTable(UpdateTableReq req, @MappingTarget DiningTable target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "lastSeenAt", ignore = true)
    Device toDevice(CreateDeviceReq req);

    @Mapping(target = "branchId", ignore = true)
    void updateDevice(UpdateDeviceReq req, @MappingTarget Device target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    BranchPosition toPosition(CreatePositionReq req);

    @Mapping(target = "branchId", ignore = true)
    void updatePosition(UpdatePositionReq req, @MappingTarget BranchPosition target);
}
