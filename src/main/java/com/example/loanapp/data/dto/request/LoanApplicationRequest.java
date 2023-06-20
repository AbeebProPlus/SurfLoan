package com.example.loanapp.data.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanApplicationRequest {
    private Long id;
    private String purpose;
    private BigDecimal amount;
    private String repaymentReference;
    private String repaymentSchedule;
    private String duration;
}