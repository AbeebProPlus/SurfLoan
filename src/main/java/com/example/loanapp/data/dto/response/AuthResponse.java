package com.example.loanapp.data.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private HttpStatus status;
    private String message;
}