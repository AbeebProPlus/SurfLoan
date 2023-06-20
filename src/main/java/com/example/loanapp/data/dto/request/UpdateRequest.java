package com.example.loanapp.data.dto.request;

import com.example.loanapp.data.model.LoanApplicationStatus;
import lombok.Data;

@Data
public class UpdateRequest {
    private Long id;
    private LoanApplicationStatus loanApplicationStatus;
}
