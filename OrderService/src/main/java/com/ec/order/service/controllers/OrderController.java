package com.ec.order.service.controllers;

import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Add order",
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
                    ))
            }
    )
    @PostMapping
    public ResponseEntity<Object> placeOrder(@Valid @RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    @Operation(
            summary = "Get all order",
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
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return orderService.getOrderById(id);
    }

    @Operation(summary = "Update order by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Order order) throws ResourceNotFoundException {
        return orderService.updateOrder(id, order);
    }

    @Operation(summary = "Delete order by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws ResourceNotFoundException {
        return orderService.deleteOrder(id);
    }

    @Operation(summary = "Get order by userId")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getOrderByUserId(@PathVariable Long userId) throws ResourceNotFoundException {
        return orderService.getOrdersByUserId(userId);
    }

    @Operation(summary = "Get order by productId")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getOrderByProductId(@PathVariable Long productId) throws ResourceNotFoundException {
        return orderService.getOrdersByProductId(productId);
    }
}