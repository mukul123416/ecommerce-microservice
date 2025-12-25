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
@Table(name = "user_balances")
@Schema(
        name = "Balance",
        description = "Balance entity for handling users balance",
        example = "{\"userId\":\"201\",\"amount\":\"999.99\"}"
)
public class UserBalance {

    @NotNull(message = "User ID cannot be null")
    @Schema(
            description = "User identifier",
            example = "201",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(nullable = false)
    @Id
    private Long userId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than zero")
    @Schema(
            description = "Balance amount",
            example = "999.99"
    )
    @Column(nullable = false)
    private Double amount;

    @Schema(
            description = "Balance date and time",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public UserBalance(Long userId, Double amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
