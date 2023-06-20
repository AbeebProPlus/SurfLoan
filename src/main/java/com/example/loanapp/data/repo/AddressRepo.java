package com.example.loanapp.data.repo;

import com.example.loanapp.data.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
