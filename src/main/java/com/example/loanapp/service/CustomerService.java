package com.example.loanapp.service;

import com.example.loanapp.data.dto.request.AuthRequest;
import com.example.loanapp.data.dto.request.LoanApplicationRequest;
import com.example.loanapp.data.dto.request.RegistrationRequest;
import com.example.loanapp.data.dto.request.ViewLoanApplicationsRequest;
import com.example.loanapp.data.dto.response.AuthResponse;
import com.example.loanapp.data.dto.response.RegistrationResponse;
import com.example.loanapp.data.dto.response.Response;
import com.example.loanapp.data.dto.response.ViewLoanApplicationStatusResponse;
import com.example.loanapp.data.model.Customer;
import com.example.loanapp.data.model.LoanAgreement;

import java.util.Optional;

public interface CustomerService {
    RegistrationResponse register(RegistrationRequest registrationRequest);
    AuthResponse authenticateAndGetToken(AuthRequest authRequest);
    Response applyForLoan(LoanApplicationRequest request);
    ViewLoanApplicationStatusResponse viewLoanApplicationStatus(ViewLoanApplicationsRequest request);
    Optional<Customer> findCustomerByEmail(String email);
    LoanAgreement viewLoanAgreement(Long id);
    Optional<Customer> findCustomerId(Long id);
    Customer save(Customer customer);
}
