package com.ec.user.service.controllers;

import com.ec.user.service.entities.User;
import com.ec.user.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.user.service.payloads.Order;
import com.ec.user.service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Place order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "Order placed, processing...",
                                      "error": false,
                                      "status": 200,
                                      "data": null
                                    }
                                    """)
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "{$error_message}",
                                      "error": true,
                                      "status": 400
                                    }
                                    """)
                    )),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "{$error_message}",
                                      "error": true,
                                      "status": 404
                                    }
                                    """)
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "{$error_message}",
                                      "error": true,
                                      "status": 500
                                    }
                                    """)
                    ))
            }
    )
    @PostMapping("/place/order")
    public ResponseEntity<Object> placeOrder(@Valid @RequestBody Order order) {
        return this.userService.placeOrder(order);
    }

    @Operation(
            summary = "Add user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "User registered successfully",
                                      "error": false,
                                      "status": 200,
                                      "data": null
                                    }
                                    """)
                    ))
            }
    )
    @PostMapping
    public ResponseEntity<Object> register(@Valid @RequestBody User user) {
        return this.userService.createUser(user);
    }

    @Operation(
            summary = "Get all user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "data": "{$data}",
                                      "message": "Fetched Successfully",
                                      "error": false,
                                      "status": 200
                                    }
                                    """)
                    ))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return this.userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return this.userService.getUserById(id);
    }

    @Operation(summary = "Get user basic details by ID")
    @GetMapping("/basic/{id}")
    public ResponseEntity<Object> fetchUserBasic(@PathVariable Long id) throws ResourceNotFoundException {
        return this.userService.fetchUserBasic(id);
    }

    @Operation(summary = "Update user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody User user) throws ResourceNotFoundException {
        return this.userService.updateUser(id, user);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws ResourceNotFoundException {
        return this.userService.deleteUser(id);
    }
}