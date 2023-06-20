package com.example.loanapp.controller;

import com.example.loanapp.data.dto.request.AuthRequest;
import com.example.loanapp.data.dto.request.LoanApplicationRequest;
import com.example.loanapp.data.dto.request.RegistrationRequest;
import com.example.loanapp.data.dto.request.ViewLoanApplicationsRequest;
import com.example.loanapp.service.CustomerService;
import com.example.loanapp.utilities.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1/customer/")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest registrationRequest,
                                                HttpServletRequest httpServletRequest){
            ApiResponse response = ApiResponse.builder()
                    .data(customerService.register(registrationRequest))
                    .timeStamp(ZonedDateTime.now())
                    .path(httpServletRequest.getRequestURI())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("authenticate")
    public ResponseEntity<ApiResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(customerService.authenticateAndGetToken(authRequest))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("loan")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse> applyForLoan(@RequestBody LoanApplicationRequest loanApplicationRequest, HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(customerService.applyForLoan(loanApplicationRequest))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("loan_status")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse> viewLoanApplicationStatus(@RequestBody ViewLoanApplicationsRequest request, HttpServletRequest httpServletRequest) {
        ApiResponse response = ApiResponse.builder()
                .data(customerService.viewLoanApplicationStatus(request))
                .timeStamp(ZonedDateTime.now())
                .path(httpServletRequest.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}