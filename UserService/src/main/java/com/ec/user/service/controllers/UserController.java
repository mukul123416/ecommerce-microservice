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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(
            summary = "Place order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Order placed, processing...\",\n" +
                                    "  \"error\": false,\n" +
                                    "  \"status\": 200,\n" +
                                    "  \"data\": null\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "404", description = "Not Found",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 404\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 500\n" +
                                    "}")
                    ))
            }
    )
    @PostMapping("/place/order")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody Order order) {
        return userService.placeOrder(order);
    }

    @Operation(
            summary = "Add user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"User registered successfully\",\n" +
                                    "  \"error\": false,\n" +
                                    "  \"status\": 200,\n" +
                                    "  \"data\": null\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "404", description = "Not Found",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 404\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 500\n" +
                                    "}")
                    ))
            }
    )
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody User user){
        return userService.createUser(user);
    }

    @Operation(
            summary = "Get all user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"data\": \"{$data}\",\n" +
                                    "  \"message\": \"Fetched Successfully\",\n" +
                                    "  \"error\": false,\n" +
                                    "  \"status\": 200\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized Access",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 401\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 500\n" +
                                    "}")
                    ))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Get user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"data\": \"{$data}\",\n" +
                                    "  \"message\": \"Fetched Successfully\",\n" +
                                    "  \"error\": false,\n" +
                                    "  \"status\": 200\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized Access",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 401\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "404", description = "Not Found",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 404\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 500\n" +
                                    "}")
                    ))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Get user basic details by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"data\": \"{$data}\",\n" +
                                    "  \"message\": \"Fetched Successfully\",\n" +
                                    "  \"error\": false,\n" +
                                    "  \"status\": 200\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized Access",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 401\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "404", description = "Not Found",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 404\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 500\n" +
                                    "}")
                    ))
            }
    )
    @GetMapping("/basic/{id}")
    public ResponseEntity<?> fetchUserBasic(@PathVariable Long id) throws ResourceNotFoundException {
        return userService.fetchUserBasic(id);
    }

    @Operation(
            summary = "Update user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Update Successful\",\n" +
                                    "  \"error\": false,\n" +
                                    "  \"status\": 200,\n" +
                                    "  \"data\": null\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized Access",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 401\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "404", description = "Not Found",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 404\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 500\n" +
                                    "}")
                    ))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) throws ResourceNotFoundException {
        return userService.updateUser(id, user);
    }

    @Operation(
            summary = "Delete user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Delete Successful\",\n" +
                                    "  \"error\": false,\n" +
                                    "  \"status\": 200,\n" +
                                    "  \"data\": null\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized Access",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 401\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 500\n" +
                                    "}")
                    ))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ResourceNotFoundException {
        return userService.deleteUser(id);
    }

}
