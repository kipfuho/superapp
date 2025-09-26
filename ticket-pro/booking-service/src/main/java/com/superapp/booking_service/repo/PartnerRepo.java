package com.superapp.booking_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superapp.booking_service.domain.Partner;

public interface PartnerRepo extends JpaRepository<Partner, String> {

}
