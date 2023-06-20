package com.example.loanapp.data.dto.response;

import com.example.loanapp.data.model.LoanApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.naming.ldap.PagedResultsControl;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewLoanApplicationStatusResponse {
    private HttpStatus status;
    private String message;
    private LoanApplicationStatus loanApplicationStatus;
    private String reason;
}