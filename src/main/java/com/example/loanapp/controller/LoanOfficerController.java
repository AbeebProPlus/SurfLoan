package com.example.loanapp.controller;

import com.example.loanapp.data.dto.request.*;
import com.example.loanapp.service.LoanOfficerService;
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
@RequestMapping("/api/v1/loan-officer/")
public class LoanOfficerController {

    private final LoanOfficerService loanOfficerService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse> authenticateAndGetToken(@RequestBody AuthRequest request,
                                                               HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanOfficerService.authenticateAndGetToken(request))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan_list")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    public ResponseEntity<ApiResponse> viewLoans(HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanOfficerService.viewAllLoanApplications())
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("loan_review")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    public ResponseEntity<ApiResponse> reviewLoanApplication(@RequestBody ReviewLoanRequest request,
                                                             HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanOfficerService.reviewLoanDetails(request))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("loan_rejection")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    public ResponseEntity<ApiResponse> rejectLoanApplication(@RequestBody RejectLoanRequest request,
                                                             HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanOfficerService.rejectLoanApplication(request))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("loan_approval")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    public ResponseEntity<ApiResponse> approveLoanApplication(@RequestBody ViewLoanApplicationsRequest request,
                                                             HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanOfficerService.approveLoanApplication(request))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("loan_agreement")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    public ResponseEntity<ApiResponse> approveLoanApplication(@RequestBody GenerateLoanAgreementRequest request,
                                                              HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanOfficerService.generateLoanAgreement(request))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("loan_status_update")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    public ResponseEntity<ApiResponse> approveLoanApplication(@RequestBody UpdateRequest request,
                                                              HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(loanOfficerService.updateLoanStatus(request))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}