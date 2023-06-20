package com.example.loanapp.data.repo;


import com.example.loanapp.data.model.LoanOfficer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanOfficerRepo extends JpaRepository<LoanOfficer, Long> {

    Optional<LoanOfficer> findByUserName(String username);
}
