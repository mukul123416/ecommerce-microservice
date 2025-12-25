package com.ec.order.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderValidateStatusDetails {

    private Long userId;

    private Long orderId;

    private String status;

    private Long productId;

    private Integer quantity;

    private String reason;

    private Double price;
}
