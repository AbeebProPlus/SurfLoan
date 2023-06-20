package com.example.loanapp.controller;
import com.example.loanapp.service.LoanService;
import com.example.loanapp.utilities.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan-agreement/")
public class LoanAgreementController {
    private final LoanService loanService;

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse> viewLoanAgreement(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanService.viewLoanAgreement(id))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}