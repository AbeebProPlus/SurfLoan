package com.example.loanapp.service;

import com.example.loanapp.data.dto.response.RegistrationResponse;
import com.example.loanapp.data.dto.response.ViewLoanApplicationStatusResponse;
import com.example.loanapp.data.model.*;
import com.example.loanapp.data.dto.request.AuthRequest;
import com.example.loanapp.data.dto.request.LoanApplicationRequest;
import com.example.loanapp.data.dto.request.RegistrationRequest;
import com.example.loanapp.data.dto.request.ViewLoanApplicationsRequest;
import com.example.loanapp.data.dto.response.AuthResponse;
import com.example.loanapp.data.dto.response.Response;
import com.example.loanapp.data.repo.AddressRepo;
import com.example.loanapp.data.repo.CustomerRepo;
import com.example.loanapp.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.loanapp.data.model.LoanApplicationStatus.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoanService loanService;

    @Override
    public RegistrationResponse register(RegistrationRequest registrationRequest) {
        try {
            Address customerAddress = getCustomerAddress(registrationRequest);
            Customer customer = getCustomerDetails(registrationRequest, customerAddress);
            addressRepo.save(customerAddress);
            customerRepo.save(customer);
            return RegistrationResponse.builder()
                    .id(customer.getId())
                    .message("Registration successful")
                    .status(HttpStatus.OK)
                    .build();
                    //response(HttpStatus.OK, "Registration successful");
        } catch (Exception ex) {
            return RegistrationResponse.builder()
                    .message("Username/Email exists")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }


    private Response response(HttpStatus httpStatus, String message) {
        return Response.builder()
                .status(httpStatus)
                .message(message)
                .build();
    }

    @Override
    public AuthResponse authenticateAndGetToken(AuthRequest authRequest) {
        try {
            String token;
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) token = jwtService.generateToken(authRequest.getUserName());
            else throw new IllegalArgumentException("User not found");
            return AuthResponse.builder()
                    .message("Authentication successful")
                    .status(HttpStatus.OK)
                    .token(token)
                    .build();
        } catch (Exception ex) {
            return AuthResponse.builder()
                    .message("Invalid userName or password")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public Response applyForLoan(LoanApplicationRequest request) {
        try {
            Customer customer = customerRepo.findById(request.getId())
                    .orElseThrow(() -> new CustomerNotFoundException("Customer doesn't exist."));
            Optional<Loan> foundLoan = loanService.findLoan(customer.getId());
            if (foundLoan.isPresent()) throw new RuntimeException("You've applied for loan previously");
            Loan loan = loanBuilder(request);
            loan.setCustomer(customer);
            loanService.save(loan);
            customer.setLoan(loan);
            customerRepo.save(customer);
            return response(HttpStatus.OK, "Loan application successful");
        } catch (Exception ex) {
            return response(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
    @Override
    public ViewLoanApplicationStatusResponse viewLoanApplicationStatus(ViewLoanApplicationsRequest request) {
        try {
            Customer customer = customerRepo.findById(request.getId())
                    .orElseThrow(() -> new CustomerNotFoundException("Customer doesn't exist"));
            return ViewLoanApplicationStatusResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Operation successful")
                    .loanApplicationStatus(customer.getLoan().getLoanApplicationStatus())
                    .reason(customer.getLoan().getReasonForRejection())
                    .build();
        } catch (Exception ex) {
            return ViewLoanApplicationStatusResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(ex.getMessage())
                    .build();
        }
    }


    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    @Override
    public LoanAgreement viewLoanAgreement(Long id) {
        return loanService.viewLoanAgreement(id);
    }

    @Override
    public Optional<Customer> findCustomerId(Long id) {
        return customerRepo.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    private Loan loanBuilder(LoanApplicationRequest request) {
        return Loan.builder()
                .amount(request.getAmount())
                .purpose(request.getPurpose())
                .repaymentReference(request.getRepaymentReference())
                .repaymentSchedule(request.getRepaymentSchedule())
                .duration(request.getDuration())
                .loanApplicationStatus(IN_PROGRESS)
                .build();
    }

    private Customer getCustomerDetails(RegistrationRequest registrationRequest, Address customerAddress) {
        Customer customer = new Customer();
        customer.setUserName(registrationRequest.getUserName());
        customer.setFirstName(registrationRequest.getFirstName());
        customer.setLastName(registrationRequest.getLastName());
        customer.setEmail(registrationRequest.getEmail());
        customer.setPhoneNumber(registrationRequest.getPhoneNumber());
        customer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        customer.setAddress(customerAddress);
        customer.setRole(String.valueOf(Role.CUSTOMER));
        return customer;
    }

    private Address getCustomerAddress(RegistrationRequest registrationRequest) {
        Address customerAddress = new Address();
        customerAddress.setHouseNumber(registrationRequest.getAddress().getHouseNumber());
        customerAddress.setCity(registrationRequest.getAddress().getCity());
        customerAddress.setCountry(registrationRequest.getAddress().getCountry());
        customerAddress.setStreetName(registrationRequest.getAddress().getStreetName());
        return customerAddress;
    }
}