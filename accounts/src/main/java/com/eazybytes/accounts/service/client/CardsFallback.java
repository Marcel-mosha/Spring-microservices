package com.eazybytes.accounts.service.client;

import com.easybank.cards.dto.CardsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.httpservice.HttpServiceFallback;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@HttpServiceFallback(value = CardsFallback.class, group = "cards")
public class CardsFallback {
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        log.warn("Cards service unavailable, fallback invoked for mobileNumber: {}", mobileNumber);
        return null;
    }
}
