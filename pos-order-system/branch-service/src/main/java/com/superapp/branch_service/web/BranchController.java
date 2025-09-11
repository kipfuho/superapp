package com.superapp.branch_service.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.branch_service.domain.Branch;
import com.superapp.branch_service.domain.Device;
import com.superapp.branch_service.domain.DiningTable;
import com.superapp.branch_service.domain.BranchPosition;
import com.superapp.branch_service.domain.TableArea;
import com.superapp.branch_service.service.BranchService;
import com.superapp.branch_service.web.dto.BranchDtos.CreateBranchReq;
import com.superapp.branch_service.web.dto.BranchDtos.UpdateBranchReq;
import com.superapp.branch_service.web.dto.DeviceDtos.CreateDeviceReq;
import com.superapp.branch_service.web.dto.DeviceDtos.UpdateDeviceReq;
import com.superapp.branch_service.web.dto.BranchPositionDtos.CreatePositionReq;
import com.superapp.branch_service.web.dto.BranchPositionDtos.UpdatePositionReq;
import com.superapp.branch_service.web.dto.TableDtos.CreateAreaReq;
import com.superapp.branch_service.web.dto.TableDtos.CreateTableReq;
import com.superapp.branch_service.web.dto.TableDtos.UpdateAreaReq;
import com.superapp.branch_service.web.dto.TableDtos.UpdateTableReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    // BRANCH
    @PostMapping("/")
    public Branch createBranch(@Valid @RequestBody CreateBranchReq req) {
        return branchService.create(req);
    }

    @PatchMapping("/{id}")
    public Branch updateBranch(@PathVariable String id, @Valid @RequestBody UpdateBranchReq req) {
        return branchService.update(id, req);
    }

    // Areas
    @PostMapping("/{branchId}/areas")
    public TableArea createArea(@PathVariable String branchId, @Valid @RequestBody CreateAreaReq req) {
        return branchService.createArea(req);
    }

    @GetMapping("/{branchId}/areas")
    public List<TableArea> listAreas(@PathVariable String branchId) {
        return branchService.listAreas(branchId);
    }

    @PatchMapping("/areas/{id}")
    public TableArea updateArea(@PathVariable String id, @Valid @RequestBody UpdateAreaReq req) {
        return branchService.updateArea(new UpdateAreaReq(id, req.name(), req.sort(), req.active()));
    }

    // Tables
    @PostMapping("/{branchId}/tables")
    public DiningTable createTable(@PathVariable String branchId, @Valid @RequestBody CreateTableReq req) {
        return branchService
                .createTable(req);
    }

    @GetMapping("/{branchId}/tables")
    public List<DiningTable> listTables(@PathVariable String branchId) {
        return branchService.listTables(branchId);
    }

    @PatchMapping("/tables/{id}")
    public DiningTable updateTable(@PathVariable String id, @Valid @RequestBody UpdateTableReq req) {
        return branchService.updateTable(new UpdateTableReq(id, req.name(), req.capacity(), req.areaId(), req.sort(),
                req.status(), req.active(), req.mergedIntoId()));
    }

    // Devices
    @PostMapping("/{branchId}/devices")
    public Device createDevice(@PathVariable String branchId, @Valid @RequestBody CreateDeviceReq req) {
        return branchService.createDevice(req);
    }

    @GetMapping("/{branchId}/devices")
    public List<Device> listDevices(@PathVariable String branchId) {
        return branchService.listDevices(branchId);
    }

    @PatchMapping("/devices/{id}")
    public Device updateDevice(@PathVariable String id, @Valid @RequestBody UpdateDeviceReq req) {
        return branchService
                .updateDevice(req);
    }

    // Positions
    @PostMapping("/{branchId}/positions")
    public BranchPosition createPosition(@PathVariable String branchId, @Valid @RequestBody CreatePositionReq req) {
        return branchService
                .createPosition(req);
    }

    @GetMapping("/{branchId}/positions")
    public List<BranchPosition> listPositions(@PathVariable String branchId) {
        return branchService.listPositions(branchId);
    }

    @PatchMapping("/positions/{id}")
    public BranchPosition updatePosition(@PathVariable String id, @Valid @RequestBody UpdatePositionReq req) {
        return branchService.updatePosition(new UpdatePositionReq(id, req.name(), req.permissions(), req.active()));
    }
}
