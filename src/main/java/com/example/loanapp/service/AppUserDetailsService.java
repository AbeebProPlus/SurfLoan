package com.example.loanapp.service;

import com.example.loanapp.config.CustomerUserDetails;
import com.example.loanapp.config.LoanOfficerDetails;
import com.example.loanapp.data.model.Customer;
import com.example.loanapp.data.model.LoanOfficer;
import com.example.loanapp.data.repo.CustomerRepo;
import com.example.loanapp.data.repo.LoanOfficerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private LoanOfficerRepo loanOfficerRepo;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepo.findByUserName(username);
        Optional<LoanOfficer> loanOfficer = loanOfficerRepo.findByUserName(username);

        if (customer.isPresent()) {
            if (customer.get().getRole().equals("CUSTOMER")) {
                return new CustomerUserDetails(customer.get());
            }
        } else if (loanOfficer.isPresent()) {
            if (loanOfficer.get().getRole().equals("LOAN_OFFICER")) {
                return new LoanOfficerDetails(loanOfficer.get());
            }
        }
        throw new UsernameNotFoundException("User not found");
    }
}
