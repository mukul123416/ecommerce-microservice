package com.ec.order.service.controllers;
import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(
            summary = "Add order",
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
    @PostMapping
    public ResponseEntity<?> placeOrder(@Valid @RequestBody Order order){
        return orderService.placeOrder(order);
    }

    @Operation(
            summary = "Get all order",
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
    @GetMapping
    public ResponseEntity<?> getAll() {
        return orderService.getAllOrders();
    }

    @Operation(
            summary = "Get order by ID",
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
        return orderService.getOrderById(id);
    }

    @Operation(
            summary = "Update order by ID",
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Order order) throws ResourceNotFoundException {
        return orderService.updateOrder(id, order);
    }

    @Operation(
            summary = "Delete order by ID",
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
        return orderService.deleteOrder(id);
    }

    @Operation(
            summary = "Get order by userId",
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable Long userId) throws ResourceNotFoundException {
        return orderService.getOrdersByUserId(userId);
    }

    @Operation(
            summary = "Get order by productId",
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
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getOrderByProductId(@PathVariable Long productId) throws ResourceNotFoundException {
        return orderService.getOrdersByProductId(productId);
    }

}
