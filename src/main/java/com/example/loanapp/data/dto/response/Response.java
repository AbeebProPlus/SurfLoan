package com.example.loanapp.data.dto.response;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private HttpStatus status;
    private String message;
}
