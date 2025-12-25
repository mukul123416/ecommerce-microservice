package com.ec.user.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private Double amount;
    private String status; // SUCCESS, FAILED
    private LocalDateTime paymentDate;
}
