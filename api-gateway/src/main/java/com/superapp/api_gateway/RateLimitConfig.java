package com.superapp.api_gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
class RateLimitConfig {
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            // Prefer X-Forwarded-For
            String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isBlank()) {
                // take first IP in case of multiple
                return Mono.just(xForwardedFor.split(",")[0].trim());
            }
            // Fall back to remote address
            return Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                    .map(addr -> addr.getAddress().getHostAddress())
                    .defaultIfEmpty("unknown");
        };
    }
}
