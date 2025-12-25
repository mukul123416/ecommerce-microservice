package com.ec.user.service.payloads;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "PlaceOrderDetails",
        description = "PlaceOrderDetails for Ecommerce application",
        example = "{\"userId\":\"23\",\"productId\":\"44\",\"quantity\":\"3\"}"
)
public class Order {

    @NotNull(message = "User ID cannot be null")
    @Schema(
            description = "User identifier",
            example = "201",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Digits(integer = 10, fraction = 0, message = "UserId must be a whole number")
    @Column(nullable = false)
    private Long userId;

    @NotNull(message = "Product ID cannot be null")
    @Schema(
            description = "Product identifier",
            example = "101",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Digits(integer = 10, fraction = 0, message = "ProductId must be a whole number")
    @Column(nullable = false)
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    @Schema(
            description = "Quantity of product ordered",
            example = "2"
    )
    @Digits(integer = 10, fraction = 0, message = "Quantity must be a whole number")
    @Column(nullable = false)
    private Integer quantity;

    private String status;
}
