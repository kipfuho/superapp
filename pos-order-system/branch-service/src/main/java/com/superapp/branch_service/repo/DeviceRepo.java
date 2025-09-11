package com.superapp.branch_service.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.Device;

public interface DeviceRepo extends MongoRepository<Device, String> {
    List<Device> findByBranchIdAndActiveOrderByLastSeenAtDesc(String branchId, boolean active);
}
