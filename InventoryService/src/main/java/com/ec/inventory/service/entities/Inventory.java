package com.ec.inventory.service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory")
@Schema(
        name = "Inventory",
        description = "Inventory entity for managing product stock",
        example = "{\"productId\":\"101\",\"quantity\":\"50\"}"
)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(
            description = "Unique inventory identifier",
            example = "301",
            required = true,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotNull(message = "Product ID cannot be null")
    @Schema(
            description = "Product identifier",
            example = "101",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(nullable = false, unique = true)
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity cannot be negative")
    @Schema(
            description = "Available stock quantity",
            example = "50"
    )
    @Column(nullable = false)
    private Integer quantity;
}