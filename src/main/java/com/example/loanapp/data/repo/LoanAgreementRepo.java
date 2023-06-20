package com.example.loanapp.data.repo;

import com.example.loanapp.data.model.LoanAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanAgreementRepo extends JpaRepository<LoanAgreement, Long> {
    LoanAgreement findByCustomerId(Long id);
}
