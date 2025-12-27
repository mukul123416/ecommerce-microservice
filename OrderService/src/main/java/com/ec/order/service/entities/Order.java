package com.ec.order.service.entities;

import com.ec.order.service.dtos.PaymentDTO;
import com.ec.order.service.dtos.ProductDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Schema(
        name = "Order",
        description = "Order entity for Ecommerce application",
        example = "{\"userId\":\"201\",\"productId\":\"101\",\"quantity\":\"2\",\"status\":\"CONFIRMED\"}"
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(
            description = "Unique order identifier",
            example = "501",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotNull(message = "User ID cannot be null")
    @Schema(
            description = "User identifier",
            example = "201",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(nullable = false)
    private Long userId;

    @NotNull(message = "Product ID cannot be null")
    @Schema(
            description = "Product identifier",
            example = "101",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(nullable = false)
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    @Schema(
            description = "Quantity of product ordered",
            example = "2"
    )
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Status cannot be null")
    @Size(min = 3, max = 30, message = "Status must be between 3 and 30 characters")
    @Schema(description = "Status for the order", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String status;

    @Schema(
            description = "Order date and time",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    @Transient
    private ProductDTO product;

    @Transient
    private PaymentDTO payment;
}
