package com.example.loanapp.data.dto.request;

import lombok.Data;

@Data
public class RejectLoanRequest {
    private Long id;
    private String reason;
}
