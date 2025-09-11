package com.superapp.branch_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.superapp.branch_service.domain.TaxRule;
import com.superapp.branch_service.repo.TaxRuleRepo;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedTaxRules(TaxRuleRepo repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(TaxRule.builder().name("No Tax").ratePct(0).inclusive(false).build());
                repo.save(TaxRule.builder().name("VAT 8%").ratePct(8).inclusive(false).build());
                repo.save(TaxRule.builder().name("VAT 8% INCLUDED").ratePct(8).inclusive(true).build());
                repo.save(TaxRule.builder().name("VAT 10%").ratePct(10).inclusive(false).build());
                repo.save(TaxRule.builder().name("VAT 10% INCLUDED").ratePct(10).inclusive(true).build());
            }
        };
    }
}
