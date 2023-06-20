package com.example.loanapp.service;

import com.example.loanapp.data.dto.request.*;
import com.example.loanapp.data.dto.response.AuthResponse;
import com.example.loanapp.data.dto.response.RegistrationResponse;
import com.example.loanapp.data.dto.response.Response;
import com.example.loanapp.data.dto.response.ViewLoanApplicationStatusResponse;
import com.example.loanapp.data.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.example.loanapp.data.model.LoanApplicationStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private LoanOfficerService loanOfficerService;

    private Address address;
    private RegistrationRequest registrationRequest;
    private LoanApplicationRequest request;



    @BeforeEach
    void setUp(){
        address = new Address();
        registrationRequest = new RegistrationRequest();
        request = new LoanApplicationRequest();

        address.setHouseNumber("45");
        address.setStreetName("meetee");
        address.setCity("rayo");
        address.setCountry("Nigeria");

        registrationRequest.setUserName("Mee");
        registrationRequest.setFirstName("Mee");
        registrationRequest.setLastName("Tee");
        registrationRequest.setEmail("meetee@gmail.com");
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
    @DisplayName("Test that customer can register")
    void register() {
        RegistrationResponse response = customerService.register(registrationRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Registration successful", response.getMessage());
    }

    @Test
    void testRegister_WithExistingEmail_ReturnsBadRequestResponse() {
        RegistrationResponse response = customerService.register(registrationRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Registration successful", response.getMessage());
        RegistrationResponse response2 = customerService.register(registrationRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatus());
        assertEquals("Username/Email exists", response2.getMessage());
    }

    @Test
    void testAuthenticateAndGetToken_WithValidCredentials_ReturnsAuthToken() {
        customerService.register(registrationRequest);
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName(registrationRequest.getUserName());
        authRequest.setPassword(registrationRequest.getPassword());
        AuthResponse response = customerService.authenticateAndGetToken(authRequest);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getToken());
        assertEquals("Authentication successful", response.getMessage());
    }

    @Test
    void testAuthenticateAndGetToken_WithInvalidCredentials_ReturnsBadRequestResponse() {
        AuthRequest request = new AuthRequest();
        request.setUserName("No name");
        request.setPassword("No password");
        AuthResponse response = customerService.authenticateAndGetToken(request);
        assertNotNull(response);
        assertEquals("Invalid userName or password", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getToken());
    }

    @Test
    void test_that_customer_can_apply_for_loan_OK_STATUS() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        Response response1 = customerService.applyForLoan(request);
        assertNotNull(response1);
        assertEquals("Loan application successful", response1.getMessage());
        assertEquals(HttpStatus.OK, response1.getStatus());
    }

    @Test
    @DisplayName("Test that customer cannot apply for loan more than once at a time")
    void test_apply_for_loan_BadRequestResponse() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        Response response1 = customerService.applyForLoan(request);
        assertNotNull(response1);
        assertEquals("Loan application successful", response1.getMessage());
        assertEquals(HttpStatus.OK, response1.getStatus());


        LoanApplicationRequest request2 = new LoanApplicationRequest();
        request2.setId(response.getId());
        request2.setPurpose("Borrowing money for marriage");
        request2.setAmount(BigDecimal.valueOf(10000));
        request2.setRepaymentReference("34567dddd");
        request2.setRepaymentSchedule("monthly");
        request2.setDuration("2 years");


        Response response2 = customerService.applyForLoan(request2);
        assertNotNull(response2);
        assertEquals("You've applied for loan previously", response2.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatus());
    }

    @Test
    public void testApplyForLoan_CustomerNotFound_BAD_REQUEST() {
        request.setId(12456L);
        Response response = customerService.applyForLoan(request);
        assertNotNull(response);
        assertEquals("Customer doesn't exist.", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("Test that customer can view loan status and get Ok status")
    void viewLoanApplicationStatus() {
        RegistrationResponse response = customerService.register(registrationRequest);
        request.setId(response.getId());
        customerService.applyForLoan(request);
        ViewLoanApplicationsRequest request1 = new ViewLoanApplicationsRequest();
        request1.setId(request.getId());
        ViewLoanApplicationStatusResponse viewLoanApplicationStatusResponse=
                customerService.viewLoanApplicationStatus(request1);
        assertNotNull(viewLoanApplicationStatusResponse);
        assertEquals(HttpStatus.OK, viewLoanApplicationStatusResponse.getStatus());
        assertEquals(IN_PROGRESS, viewLoanApplicationStatusResponse.getLoanApplicationStatus());
    }

}