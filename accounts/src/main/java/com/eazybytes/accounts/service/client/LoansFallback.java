package com.eazybytes.accounts.service.client;

import com.easybank.loans.dto.LoansDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.httpservice.HttpServiceFallback;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@HttpServiceFallback(value = LoansFallback.class, group = "loans")
public class LoansFallback {
    public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        log.warn("Loans service unavailable, fallback invoked for mobileNumber: {}", mobileNumber);
        return null;
    }
}
