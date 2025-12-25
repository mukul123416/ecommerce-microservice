package com.ec.user.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private Integer quantity;
    private LocalDateTime orderDate;
    private String status;
    private ProductDTO product;
    private PaymentDTO payment;

}
