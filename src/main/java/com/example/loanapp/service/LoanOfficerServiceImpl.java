package com.example.loanapp.service;

import com.example.loanapp.data.dto.request.*;
import com.example.loanapp.data.dto.response.AuthResponse;
import com.example.loanapp.data.dto.response.Response;
import com.example.loanapp.data.dto.response.ReviewLoanResponse;
import com.example.loanapp.data.model.*;
import com.example.loanapp.data.repo.LoanOfficerRepo;
import com.example.loanapp.exceptions.CustomerNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanOfficerServiceImpl implements LoanOfficerService{

    private final LoanOfficerRepo loanOfficerRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final LoanService loanService;
    private final CustomerService customerService;

    @Override
    public List<Loan> viewAllLoanApplications() {
        return loanService.viewAllLoanApplications();
    }

    @Override
    public Response approveLoanApplication(ViewLoanApplicationsRequest request) {
        try {
            Loan loan = loanService.findLoan(request.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid loan details"));
            loan.setLoanApplicationStatus(LoanApplicationStatus.APPROVED);
            loanService.save(loan);
            return response(HttpStatus.OK, "Loan approval successful");
        }catch (Exception ex){
            return response(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    private Response response(HttpStatus httpStatus, String message) {
        return Response.builder()
                .status(httpStatus)
                .message(message)
                .build();
    }

    @Override
    public Response rejectLoanApplication(RejectLoanRequest request) {
        try {
            Loan loan = loanService.findLoan(request.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid loan id"));
            loan.setLoanApplicationStatus(LoanApplicationStatus.REJECTED);
            loan.setReasonForRejection(request.getReason());
            loanService.save(loan);
            return response(HttpStatus.OK, "Loan rejection successful");
        }catch (Exception ex){
            return response(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @Override
    public ReviewLoanResponse reviewLoanDetails(ReviewLoanRequest request) {
        try {
            Loan loan = loanService.reviewLoanDetails(request.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid loan details"));
            return ReviewLoanResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Operation successful")
                    .loan(loan)
                    .build();
        }catch (Exception ex){
            return ReviewLoanResponse.builder()
                    .message(ex.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public AuthResponse authenticateAndGetToken(AuthRequest authRequest) {
        try {
            String token;
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(),
                            authRequest.getPassword()));
            if (authentication.isAuthenticated()) token = jwtService.generateToken(authRequest.getUserName());
            else throw new IllegalArgumentException("User not found");
            return AuthResponse.builder()
                    .message("Authentication successful")
                    .status(HttpStatus.OK)
                    .token(token)
                    .build();
        }catch (Exception ex){
            return AuthResponse.builder()
                    .message("Invalid userName or password")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public Response generateLoanAgreement(GenerateLoanAgreementRequest request) {
        try {
            Customer customer = customerService.findCustomerByEmail(request.getCustomerEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid customer detail"));
            if (!customer.getLoan().getLoanApplicationStatus().equals(LoanApplicationStatus.APPROVED))
                throw new RuntimeException("This customer's loan is not approved");
            Loan loan = loanService.findLoan(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid customer detail"));
            LoanAgreement loanAgreement = createLoanAgreement(request, customer, loan);
            loanService.saveLoanAgreement(loanAgreement);
            return response(HttpStatus.OK, "Agreement generated successful");
        }catch (Exception ex){
            return response(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @Override
    public Response updateLoanStatus(UpdateRequest request) {
        try {
            Customer customer = customerService.findCustomerId(request.getId())
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

            customer.getLoan().setLoanApplicationStatus(request.getLoanApplicationStatus());
            loanService.save(customer.getLoan());
            customerService.save(customer);
            return response(HttpStatus.OK, "Operation successful");
        }catch (Exception ex){
            return response(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    private LoanAgreement createLoanAgreement(GenerateLoanAgreementRequest request, Customer customer, Loan loan) {
        return LoanAgreement.builder()
                .borrowerName(customer.getFirstName() + " " + customer.getLastName())
                .email(request.getCustomerEmail())
                .loanAmount(loan.getAmount())
                .borrowerPhoneNumber(customer.getEmail())
                .loanDuration(request.getLoanDuration())
                .interestRate(request.getInterestRate())
                .paymentMode(request.getPaymentMode())
                .lenderName(request.getLenderName())
                .customerId(customer.getId())
                .build();
    }

    @PostConstruct
    private void createLoanOfficer() {
        if (!loanOfficerExists("loanOfficer1")) {
            LoanOfficer loanOfficer = LoanOfficer.builder()
                    .userName("loanOfficer1")
                    .password(passwordEncoder.encode("loanOfficer1"))
                    .role(String.valueOf(Role.LOAN_OFFICER))
                    .build();
            loanOfficerRepo.save(loanOfficer);
        }
    }

    private boolean loanOfficerExists(String userName) {
        Optional<LoanOfficer> loanOfficer = loanOfficerRepo.findByUserName(userName);
        return loanOfficer.isPresent();
    }
}