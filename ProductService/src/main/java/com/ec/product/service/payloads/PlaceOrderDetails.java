package com.ec.product.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDetails {

    private Long orderId;

    private Long userId;

    private Long productId;

    private Integer quantity;
}
