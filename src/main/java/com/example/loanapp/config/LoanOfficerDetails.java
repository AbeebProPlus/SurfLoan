package com.example.loanapp.config;

import com.example.loanapp.data.model.LoanOfficer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LoanOfficerDetails implements UserDetails {
    private String name;
    private String password;
    private List<GrantedAuthority> authorityList;

    public LoanOfficerDetails(LoanOfficer loanOfficer) {
        this.name = loanOfficer.getUserName();
        this.password = loanOfficer.getPassword();
        authorityList = Arrays.stream(loanOfficer.getRole().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
