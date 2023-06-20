package com.example.loanapp.service;

import com.example.loanapp.data.model.Loan;
import com.example.loanapp.data.model.LoanAgreement;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<Loan> viewAllLoanApplications();
    Optional<Loan> reviewLoanDetails(Long id);
    Optional<Loan> findLoan(Long id);
    void save(Loan loan);
    void saveLoanAgreement(LoanAgreement loanAgreement);
    LoanAgreement viewLoanAgreement(Long id);
}
