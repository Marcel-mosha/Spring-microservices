package com.eazybytes.gatewayserver.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;

@RestController
public class FallbackConroller {
    private static final Logger logger = LoggerFactory.getLogger(FallbackConroller.class);

    @RequestMapping("/contactSupport")
    public Mono<String> contactSupport(ServerWebExchange exchange) {
        Throwable cause = exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
        logger.warn("Circuit breaker fallback invoked for {}: {}",
                exchange.getRequest().getPath(),
                cause != null ? cause.toString() : "circuit open / no exception captured");
        return Mono.just("An error occurred. Please try after some time or contact support for assistance.");
    }
}
