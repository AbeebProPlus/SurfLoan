package com.example.loanapp.service;

import com.example.loanapp.data.dto.request.*;
import com.example.loanapp.data.dto.response.AuthResponse;
import com.example.loanapp.data.dto.response.Response;
import com.example.loanapp.data.dto.response.ReviewLoanResponse;
import com.example.loanapp.data.model.Loan;
import org.hibernate.sql.Update;

import java.util.List;

public interface LoanOfficerService {
   List<Loan> viewAllLoanApplications();

   Response approveLoanApplication(ViewLoanApplicationsRequest request);
   Response rejectLoanApplication(RejectLoanRequest request);
   ReviewLoanResponse reviewLoanDetails(ReviewLoanRequest request);
   AuthResponse authenticateAndGetToken(AuthRequest authRequest);
   Response generateLoanAgreement(GenerateLoanAgreementRequest request);
   Response updateLoanStatus(UpdateRequest request);
}
