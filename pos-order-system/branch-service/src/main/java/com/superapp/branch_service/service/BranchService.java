package com.superapp.branch_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.Branch;
import com.superapp.branch_service.domain.Device;
import com.superapp.branch_service.domain.DiningTable;
import com.superapp.branch_service.domain.BranchPosition;
import com.superapp.branch_service.domain.TableArea;
import com.superapp.branch_service.repo.BranchRepo;
import com.superapp.branch_service.repo.DeviceRepo;
import com.superapp.branch_service.repo.DiningTableRepo;
import com.superapp.branch_service.repo.PositionRepo;
import com.superapp.branch_service.repo.TableAreaRepo;
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
import com.superapp.branch_service.web.mapper.BranchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepo branchRepo;
    private final TableAreaRepo areaRepo;
    private final DiningTableRepo tableRepo;
    private final DeviceRepo deviceRepo;
    private final PositionRepo positionRepo;
    private final BranchMapper mapper;

    // Branches
    public List<Branch> list() {
        return branchRepo.findAll();
    }

    public List<Branch> listByBrandId(String brandId) {
        return branchRepo.findByBrandId(brandId);
    }

    public Branch create(CreateBranchReq req) {
        if (branchRepo.existsByCode(req.code())) {
            throw new IllegalArgumentException("Branch with code already exists");
        }
        Branch branch = mapper.toBranch(req);
        Branch saved = branchRepo.save(branch);
        return saved;
    }

    public Branch update(String id, UpdateBranchReq req) {
        Branch b = branchRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));
        mapper.updateBranch(req, b);
        return branchRepo.save(b);
    }

    // Areas
    public TableArea createArea(CreateAreaReq req) {
        TableArea a = mapper.toArea(req);
        return areaRepo.save(a);
    }

    public TableArea updateArea(UpdateAreaReq req) {
        TableArea a = areaRepo.findById(req.id()).orElseThrow(() -> new NoSuchElementException("TableArea not found"));
        mapper.updateArea(req, a);
        return areaRepo.save(a);
    }

    public List<TableArea> listAreas(String branchId) {
        return areaRepo.findByBranchIdAndActiveOrderBySortAsc(branchId, true);
    }

    // Tables
    public DiningTable createTable(CreateTableReq req) {
        if (tableRepo.existsByCode(req.code())) {
            throw new IllegalArgumentException("DiningTable with name already exists");
        }
        DiningTable t = mapper.toTable(req);
        return tableRepo.save(t);
    }

    public DiningTable updateTable(UpdateTableReq req) {
        DiningTable t = tableRepo.findById(req.id()).orElseThrow(() -> new NoSuchElementException("Table not found"));
        mapper.updateTable(req, t);
        return tableRepo.save(t);
    }

    public List<DiningTable> listTables(String branchId) {
        return tableRepo.findByBranchIdAndActiveOrderBySortAsc(branchId, true);
    }

    // Devices
    public Device createDevice(CreateDeviceReq req) {
        Device d = mapper.toDevice(req);
        return deviceRepo.save(d);
    }

    public Device updateDevice(UpdateDeviceReq req) {
        Device d = deviceRepo.findById(req.id()).orElseThrow();
        mapper.updateDevice(req, d);
        return deviceRepo.save(d);
    }

    public List<Device> listDevices(String branchId) {
        return deviceRepo.findByBranchIdAndActiveOrderByLastSeenAtDesc(branchId, true);
    }

    // Positions
    public BranchPosition createPosition(CreatePositionReq req) {
        BranchPosition p = mapper.toPosition(req);
        return positionRepo.save(p);
    }

    public BranchPosition updatePosition(UpdatePositionReq req) {
        BranchPosition p = positionRepo.findById(req.id()).orElseThrow();
        mapper.updatePosition(req, p);
        return positionRepo.save(p);
    }

    public List<BranchPosition> listPositions(String branchId) {
        return positionRepo.findByBranchIdOrBranchIdIsNull(branchId);
    }
}
