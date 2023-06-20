package com.example.loanapp.data.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateLoanAgreementRequest {
    private String customerEmail;
    private String interestRate;
    private String loanDuration;
    private String paymentMode;
    private String lenderName;
}
