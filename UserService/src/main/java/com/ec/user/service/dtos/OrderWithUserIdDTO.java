package com.ec.user.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithUserIdDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String status;
    private LocalDateTime orderDate;
    private ProductDTO product;
    private PaymentDTO payment;
}
