package com.ec.inventory.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPriceCalDetails {

    private Long userId;

    private Long orderId;

    private Double price;

    private Long productId;

    private Integer quantity;
}
