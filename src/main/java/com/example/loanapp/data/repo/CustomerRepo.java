package com.example.loanapp.data.repo;

import com.example.loanapp.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    Optional<Customer> findById(Long id);
    Optional<Customer> findByUserName(String userName);
    Optional<Customer> findByEmail(String email);

}
