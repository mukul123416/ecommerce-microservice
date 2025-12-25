package com.ec.order.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusDetails {

    private Long orderId;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private Double amount;

    private String status;
}
