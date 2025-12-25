package com.ec.product.service.entities;

import com.ec.product.service.dtos.InventoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@Schema(
        name = "Product",
        description = "Product entity for Ecommerce application",
        example = "{\"orderId\":\"101\",\"name\":\"Minimalist Gold-Plated Necklace\",\"description\":\"Description for the product\",\"price\":\"1999.99\"}"
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique product identifier", example = "101", required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Positive(message = "Price must be greater than zero")
    @Schema(description = "Unique order identifier", example = "101",accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long orderId;

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Column(nullable = false)
    @Schema(description = "Product name", example = "Minimalist Gold-Plated Necklace", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "Description cannot be null")
    @Size(min = 3, max = 80, message = "Description must be between 3 and 80 characters")
    @Schema(description = "Description for the product", required = true)
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Price amount cannot be null")
    @Positive(message = "Price must be greater than zero")
    @Schema(description = "Price amount", example = "1999.99")
    @Column(nullable = false)
    private Double price;

    @Transient
    private InventoryDTO inventory;
}
