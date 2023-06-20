package com.example.loanapp.service;

import com.example.loanapp.data.model.Loan;
import com.example.loanapp.data.model.LoanAgreement;
import com.example.loanapp.data.repo.LoanAgreementRepo;
import com.example.loanapp.data.repo.LoanRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService{
    private final LoanRepo loanRepo;
    private final LoanAgreementRepo loanAgreementRepo;

    @Override
    public List<Loan> viewAllLoanApplications() {
        return loanRepo.findAll();
    }

    @Override
    public Optional<Loan> reviewLoanDetails(Long id) {
        return loanRepo.findById(id);
    }

    @Override
    public Optional<Loan> findLoan(Long id) {
        return loanRepo.findById(id);
    }

    @Override
    public void save(Loan loan) {
        loanRepo.save(loan);
    }

    @Override
    public void saveLoanAgreement(LoanAgreement loanAgreement) {
        loanAgreementRepo.save(loanAgreement);
    }

    @Override
    public LoanAgreement viewLoanAgreement(Long id) {
        return loanAgreementRepo.findByCustomerId(id);
    }
}
