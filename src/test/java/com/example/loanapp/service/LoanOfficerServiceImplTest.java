package com.example.loanapp.service;

import com.example.loanapp.data.dto.request.*;
import com.example.loanapp.data.dto.response.AuthResponse;
import com.example.loanapp.data.dto.response.RegistrationResponse;
import com.example.loanapp.data.dto.response.Response;
import com.example.loanapp.data.dto.response.ReviewLoanResponse;
import com.example.loanapp.data.model.Address;
import com.example.loanapp.data.model.Customer;
import com.example.loanapp.data.model.Loan;
import com.example.loanapp.data.model.LoanAgreement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.loanapp.data.model.LoanApplicationStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class LoanOfficerServiceImplTest {

    @Autowired
    private LoanOfficerService loanOfficerService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanService loanService;

    private RegistrationRequest registrationRequest;
     private Address address;
     private LoanApplicationRequest request;

    @BeforeEach
    void setUp(){
        registrationRequest = new RegistrationRequest();
        address = new Address();
        request = new LoanApplicationRequest();

        address.setHouseNumber("45");
        address.setStreetName("meetee");
        address.setCity("rayo");
        address.setCountry("Nigeria");

        registrationRequest.setUserName("Borrower455");
        registrationRequest.setFirstName("UserName");
        registrationRequest.setLastName("UserName");
        registrationRequest.setEmail("borrower455@gmail.com");
        registrationRequest.setPassword("123456");
        registrationRequest.setPhoneNumber("08100437363");
        registrationRequest.setAddress(address);

        request.setPurpose("Borrowing money for food");
        request.setAmount(BigDecimal.valueOf(2000));
        request.setRepaymentReference("345678899");
        request.setRepaymentSchedule("monthly");
        request.setDuration("5 years");

    }

    @Test
    void test_that_loan_officer_can_login() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("loanOfficer1");
        authRequest.setPassword("loanOfficer1");
        AuthResponse response = loanOfficerService.authenticateAndGetToken(authRequest);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getToken());
        assertEquals("Authentication successful", response.getMessage());
    }
    @Test
    void test_that_invalid_loan_officer_cannot_login() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("loanOff");
        authRequest.setPassword("loanOff");
        AuthResponse response = loanOfficerService.authenticateAndGetToken(authRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getToken());
        assertEquals("Invalid userName or password", response.getMessage());
    }

    @Test
    void approveLoanApplication_OK_STATUS() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        ViewLoanApplicationsRequest viewLoanApplicationsRequest = new ViewLoanApplicationsRequest();
        viewLoanApplicationsRequest.setId(response.getId());
        Response response1 = loanOfficerService.approveLoanApplication(viewLoanApplicationsRequest);
        assertNotNull(response1);
        assertEquals(HttpStatus.OK, response1.getStatus());
        assertEquals("Loan approval successful", response1.getMessage());
        Customer customer = customerService.findCustomerId(response.getId()).get();
        assertEquals(APPROVED, customer.getLoan().getLoanApplicationStatus());
    }

    @Test
    void test_that_loan_officer_can_reject_loan_application() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        RejectLoanRequest rejectLoanRequest = new RejectLoanRequest();
        rejectLoanRequest.setId(response.getId());
        rejectLoanRequest.setReason("");
        Response response1 = loanOfficerService.rejectLoanApplication(rejectLoanRequest);
        assertNotNull(response1);
        assertEquals(HttpStatus.OK, response1.getStatus());
        assertEquals("Loan rejection successful", response1.getMessage());
        Customer customer = customerService.findCustomerId(response.getId()).get();
        assertEquals(REJECTED, customer.getLoan().getLoanApplicationStatus());
    }

    @Test
    void test_reviewLoanDetails_with_valid_loan_details_gives_OK_STATUS() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        ReviewLoanRequest reviewLoanRequest = new ReviewLoanRequest();
        reviewLoanRequest.setId(response.getId());
        ReviewLoanResponse reviewLoanResponse = loanOfficerService.reviewLoanDetails(reviewLoanRequest);
        assertNotNull(reviewLoanResponse);
        assertEquals(HttpStatus.OK, reviewLoanResponse.getHttpStatus());
        assertEquals("Operation successful", reviewLoanResponse.getMessage());
    }

    @Test
    void test_reviewLoanDetails_withInvalid_details_gets_BadRequest_Status() {
        request.setId(1000L);
        customerService.applyForLoan(request);
        ReviewLoanRequest reviewLoanRequest = new ReviewLoanRequest();
        reviewLoanRequest.setId(request.getId());
        ReviewLoanResponse reviewLoanResponse = loanOfficerService.reviewLoanDetails(reviewLoanRequest);
        assertNotNull(reviewLoanResponse);
        assertEquals(HttpStatus.BAD_REQUEST, reviewLoanResponse.getHttpStatus());
        assertEquals("Invalid loan details", reviewLoanResponse.getMessage());
    }

    @Test
    void test_that_loan_officer_can_generateLoanAgreement_for_approved_loan_Ok_Status() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        ViewLoanApplicationsRequest viewLoanApplicationsRequest = new ViewLoanApplicationsRequest();
        viewLoanApplicationsRequest.setId(response.getId());
        loanOfficerService.approveLoanApplication(viewLoanApplicationsRequest);
        Customer customer = customerService.findCustomerId(response.getId()).get();
        GenerateLoanAgreementRequest request1 = new GenerateLoanAgreementRequest();
        request1.setCustomerEmail(customer.getEmail());
        request1.setLoanDuration("5");
        request1.setInterestRate("16%");
        request1.setLenderName("Lender name");
        request1.setPaymentMode("Month");
        Response response1 = loanOfficerService.generateLoanAgreement(request1);
        LoanAgreement loanAgreement = loanService.viewLoanAgreement(customer.getId());
        assertNotNull(loanAgreement);
        assertNotNull(response1);
        assertEquals(HttpStatus.OK, response1.getStatus());
        assertEquals("Agreement generated successful", response1.getMessage());
    }
    @Test
    void test_that_loan_officer_cannot_generateLoanAgreement_for_rejected_application() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        RejectLoanRequest rejectLoanRequest = new RejectLoanRequest();
        rejectLoanRequest.setId(response.getId());
        rejectLoanRequest.setReason("");
        loanOfficerService.rejectLoanApplication(rejectLoanRequest);
        Customer customer = customerService.findCustomerId(response.getId()).get();
        GenerateLoanAgreementRequest request1 = new GenerateLoanAgreementRequest();
        request1.setCustomerEmail(customer.getEmail());
        request1.setLoanDuration("5");
        request1.setInterestRate("16%");
        request1.setLenderName("Lender name");
        request1.setPaymentMode("Month");
        Response response2 = loanOfficerService.generateLoanAgreement(request1);
        LoanAgreement loanAgreement = loanService.viewLoanAgreement(customer.getId());
        assertNull(loanAgreement);
        assertNotNull(response2);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatus());
        assertEquals("This customer's loan is not approved", response2.getMessage());
    }

    @Test
    void test_that_loan_officer_cannot_generateLoanAgreement_for_customer_that_did_not_apply_for_loan() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        Customer customer = customerService.findCustomerId(response.getId()).get();
        GenerateLoanAgreementRequest request1 = new GenerateLoanAgreementRequest();
        request1.setCustomerEmail(customer.getEmail());
        request1.setLoanDuration("5");
        request1.setInterestRate("16%");
        request1.setLenderName("Lender name");
        request1.setPaymentMode("Month");
        Response response2 = loanOfficerService.generateLoanAgreement(request1);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatus());
    }

    @Test
    void test_that_loan_officer_can_updateLoanStatus_gets_status_OK() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        Optional<Customer> customer = customerService.findCustomerId(response.getId());
        assertEquals(IN_PROGRESS, customer.get().getLoan().getLoanApplicationStatus());
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setLoanApplicationStatus(APPROVED);
        Response response1 = loanOfficerService.updateLoanStatus(updateRequest);
        Optional<Customer> customer1 = customerService.findCustomerId(response.getId());
        assertEquals(APPROVED, customer1.get().getLoan().getLoanApplicationStatus());
        assertEquals(HttpStatus.OK, response1.getStatus());
    }

    @Test
    void test_that_loan_officer_can_viewAllLoanApplications_status_Ok() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        RegistrationRequest registrationRequestTwo = new RegistrationRequest();
        registrationRequestTwo.setUserName("Borrower555");
        registrationRequestTwo.setFirstName("UserName");
        registrationRequestTwo.setLastName("UserName");
        registrationRequestTwo.setEmail("borrower555@gmail.com");
        registrationRequestTwo.setPassword("123456");
        registrationRequestTwo.setPhoneNumber("08100437363");
        registrationRequestTwo.setAddress(address);
        RegistrationResponse response2 = customerService.register(registrationRequestTwo);
        request.setId(response2.getId());
        customerService.applyForLoan(request);
        List<Loan> loanApplications = loanOfficerService.viewAllLoanApplications();
        assertEquals(2, loanApplications.size());
    }
}