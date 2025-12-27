package com.ec.user.service.entities;

import com.ec.user.service.dtos.OrderDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Schema(
        name = "User",
        description = "User entity for Ecommerce application",
        example = "{\"name\":\"Mukul Sharma\",\"about\":\"Hello, my name is Mukul Sharma\",\"email\":\"abc@domain.com\",\"password\":\"P@ssw0rd\"}"
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique user identifier", example = "101", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 10 characters")
    @Column(nullable = false,unique = true)
    @Schema(description = "User's name", example = "Mukul Sharma", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "About cannot be null")
    @Size(min = 3, max = 30, message = "About must be between 3 and 30 characters")
    @Schema(description = "About for the user", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String about;

    @NotNull(message = "Email cannot be null")
    @Column(unique = true,nullable = false)
    @Schema(description = "User's email address", example = "mukul@example.com", format = "email", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "User password (write only)", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.WRITE_ONLY, example = "P@ssw0rd")
    private String password;

    @Transient
    private List<OrderDTO> order = new ArrayList<>();

}
