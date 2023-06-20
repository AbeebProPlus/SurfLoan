package com.example.loanapp.data.dto.response;

import com.example.loanapp.data.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLoanResponse {
    private HttpStatus httpStatus;
    private String message;
    private Loan loan;
}
