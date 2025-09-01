package com.superapp.api_gateway;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;

@RestController
class FallbackController {

    @GetMapping(value = "/fallback/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> usersFallback(ServerWebExchange exchange) {
        Throwable cause = exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE; // default 503
        if (cause instanceof TimeoutException) {
            status = HttpStatus.GATEWAY_TIMEOUT; // 504
        } else if (cause instanceof ConnectException) {
            status = HttpStatus.BAD_GATEWAY; // 502
        } else if (isCallNotPermitted(cause)) {
            status = HttpStatus.SERVICE_UNAVAILABLE; // 503 when CB is OPEN
        }

        Map<String, Object> body = Map.of(
                "message", "User service temporarily unavailable",
                "status", "fallback",
                "service", "user-service",
                "error", cause != null ? cause.getClass().getSimpleName() : "n/a");

        return Mono.just(
                ResponseEntity.status(status)
                        .header("X-Fallback", "userServiceBreaker")
                        .body(body));
    }

    private boolean isCallNotPermitted(Throwable t) {
        // Resilience4j class is optional; avoid hard dependency
        return t != null && t.getClass().getSimpleName().equals("CallNotPermittedException");
    }
}
