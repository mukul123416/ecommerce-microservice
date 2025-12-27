package com.ec.payment.service.controllers;

import com.ec.payment.service.entities.Payment;
import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.services.BalanceService;
import com.ec.payment.service.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final BalanceService balanceService;

    public PaymentController(PaymentService paymentService, BalanceService balanceService) {
        this.paymentService = paymentService;
        this.balanceService = balanceService;
    }

    @Operation(
            summary = "Add payment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "Payment add successfully",
                                      "error": false,
                                      "status": 200,
                                      "data": null
                                    }
                                    """)
                    ))
            }
    )
    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody Payment payment) {
        return paymentService.addPayment(payment);
    }

    @Operation(summary = "Get all payment")
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return paymentService.getAllPayment();
    }

    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return paymentService.getPaymentById(id);
    }

    @Operation(summary = "Update payment by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Payment payment) throws ResourceNotFoundException {
        return paymentService.updatePayment(id, payment);
    }

    @Operation(summary = "Delete payment by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws ResourceNotFoundException {
        return paymentService.deletePayment(id);
    }

    @Operation(
            summary = "Get multiple payments by Order IDs",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "orderId": 101,
                                                "userId": 5,
                                                "amount": 500.0,
                                                "status": "SUCCESS",
                                                "paymentDate": "2025-12-15 11:25:12.73637"
                                              }
                                            ]
                                            """
                            )
                    ))
            }
    )
    @GetMapping("/batch")
    public ResponseEntity<List<Payment>> getPaymentByOrderIds(@RequestParam List<Long> orderIds) {
        List<Payment> payments = paymentService.getPaymentByOrderIds(orderIds);
        return ResponseEntity.ok(payments);
    }

    @Operation(summary = "Add balance")
    @PostMapping("/balance")
    public ResponseEntity<Object> addBalance(@Valid @RequestBody UserBalance userBalance) {
        return balanceService.addBalance(userBalance);
    }

    @Operation(summary = "Get all balance")
    @GetMapping("/balance")
    public ResponseEntity<Object> getAllBalance() {
        return balanceService.getAllBalance();
    }

    @Operation(summary = "Get balance by ID")
    @GetMapping("/balance/{id}")
    public ResponseEntity<Object> getBalanceById(@PathVariable Long id) throws ResourceNotFoundException {
        return balanceService.getBalanceById(id);
    }
}