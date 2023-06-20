package com.example.loanapp.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String borrowerName;
    @Column(nullable = false)
    private String borrowerPhoneNumber;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private BigDecimal loanAmount;
    @Column(nullable = false)
    private String interestRate;
    @Column(nullable = false)
    private String loanDuration;
    @Column(nullable = false)
    private String paymentMode;
    @Column(nullable = false)
    private String lenderName;
    @Column(nullable = false, unique = true)
    private Long customerId;
}
