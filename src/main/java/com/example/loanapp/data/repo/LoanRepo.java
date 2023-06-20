package com.example.loanapp.data.repo;

import com.example.loanapp.data.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepo extends JpaRepository<Loan, Long> {
}