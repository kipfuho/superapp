package com.superapp.branch_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.TaxRule;
import com.superapp.branch_service.repo.TaxRuleRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxRuleService {
    private final TaxRuleRepo repo;

    public List<TaxRule> list() {
        return repo.findAll();
    }

    public TaxRule create(TaxRule rule) {
        return repo.save(rule);
    }

    public TaxRule update(String id, TaxRule rule) {
        TaxRule existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TaxRule not found"));
        existing.setName(rule.getName());
        existing.setRatePct(rule.getRatePct());
        existing.setInclusive(rule.isInclusive());
        return repo.save(existing);
    }
}
