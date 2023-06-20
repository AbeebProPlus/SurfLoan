package com.example.loanapp.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String purpose;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String duration;
    @Column(nullable = false)
    private String repaymentSchedule;
    @Column(nullable = false)
    private String repaymentReference;
    @OneToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus loanApplicationStatus;
    private String reasonForRejection;
}
