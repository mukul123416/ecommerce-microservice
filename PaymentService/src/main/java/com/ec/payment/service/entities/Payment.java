package com.ec.payment.service.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@Schema(
        name = "Payment",
        description = "Payment entity for handling order payments",
        example = "{\"orderId\":\"501\",\"userId\":\"201\",\"amount\":\"999.99\",\"status\":\"SUCCESS\"}"
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(
            description = "Unique payment identifier",
            example = "701",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotNull(message = "Order ID cannot be null")
    @Schema(
            description = "Associated order identifier",
            example = "501",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(nullable = false)
    private Long orderId;

    @NotNull(message = "User ID cannot be null")
    @Schema(
            description = "User identifier",
            example = "201",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(nullable = false)
    private Long userId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than zero")
    @Schema(
            description = "Payment amount",
            example = "999.99"
    )
    @Column(nullable = false)
    private Double amount;

    @NotNull(message = "Payment status cannot be null")
    @Schema(
            description = "Payment status",
            example = "SUCCESS",
            allowableValues = {"SUCCESS", "FAILED"}
    )
    @Column(nullable = false)
    private String status;

    @Schema(
            description = "Payment date and time",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @PrePersist
    protected void onCreate() {
        this.paymentDate = LocalDateTime.now();
    }
}