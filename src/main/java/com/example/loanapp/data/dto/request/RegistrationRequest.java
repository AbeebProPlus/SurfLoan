package com.example.loanapp.data.dto.request;

import com.example.loanapp.data.model.Address;
import lombok.Data;

@Data
public class RegistrationRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
    private String phoneNumber;
    private String password;
}
