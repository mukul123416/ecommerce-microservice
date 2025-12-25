package com.ec.order.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private Long userId;
    private Double amount;
    private String status; // SUCCESS, FAILED
    private LocalDateTime paymentDate;
}
