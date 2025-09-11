package com.superapp.branch_service.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.TaxRule;

public interface TaxRuleRepo extends MongoRepository<TaxRule, String> {
}
