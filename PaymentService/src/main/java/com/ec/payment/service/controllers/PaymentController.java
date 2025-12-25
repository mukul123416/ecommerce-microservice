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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BalanceService balanceService;

    @Operation(
            summary = "Add payment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Payment add successfully\",\n" +
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
    public ResponseEntity<?> add(@Valid @RequestBody Payment payment){
        return paymentService.addPayment(payment);
    }

    @Operation(
            summary = "Get all payment",
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
        return paymentService.getAllPayment();
    }

    @Operation(
            summary = "Get payment by ID",
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
        return paymentService.getPaymentById(id);
    }

    @Operation(
            summary = "Update payment by ID",
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Payment payment) throws ResourceNotFoundException {
        return paymentService.updatePayment(id, payment);
    }

    @Operation(
            summary = "Delete payment by ID",
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
        return paymentService.deletePayment(id);
    }

    // Batch endpoint
    @Operation(
            summary = "Get multiple payments by Order IDs",
            description = "Fetch a list of payment records for the provided list of Order IDs",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "[" +
                                            "  {" +
                                            "    \"id\": 1," +
                                            "    \"orderId\": 101," +
                                            "    \"userId\": 5," +
                                            "    \"amount\": 500.0," +
                                            "    \"status\": \"SUCCESS\"," + // Comma added
                                            "    \"paymentDate\": \"2025-12-15 11:25:12.73637\"" +
                                            "  }," +
                                            "  {" +
                                            "    \"id\": 2," +
                                            "    \"orderId\": 102," +
                                            "    \"userId\": 5," +
                                            "    \"amount\": 1200.0," +
                                            "    \"status\": \"PENDING\"," +
                                            "    \"paymentDate\": \"2025-12-15 11:25:12.73637\"" +
                                            "  }" +
                                            "]"
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid IDs", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "  \"message\": \"Invalid list of order IDs\"," +
                                            "  \"error\": true," +
                                            "  \"status\": 400" +
                                            "}"
                            )
                    )),
                    @ApiResponse(responseCode = "500", description = "Server Error", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "  \"message\": \"Internal server error occurred\"," +
                                            "  \"error\": true," +
                                            "  \"status\": 500" +
                                            "}"
                            )
                    ))
            }
    )
    @GetMapping("/batch")
    public ResponseEntity<List<Payment>> getPaymentByOrderIds(@RequestParam List<Long> orderIds) {
        List<Payment> payments = paymentService.getPaymentByOrderIds(orderIds);
        return ResponseEntity.ok(payments);
    }

    @Operation(
            summary = "Add balance",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Balance Updated! Total: 344.67\",\n" +
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
    @PostMapping("/balance")
    public ResponseEntity<?> addBalance(@Valid @RequestBody UserBalance userBalance){
        return balanceService.addBalance(userBalance);
    }

    @Operation(
            summary = "Get all balance",
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
    @GetMapping("/balance")
    public ResponseEntity<?> getAllBalance() {
        return balanceService.getAllBalance();
    }

    @Operation(
            summary = "Get balance by ID",
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
    @GetMapping("/balance/{id}")
    public ResponseEntity<?> getBalanceById(@PathVariable Long id) throws ResourceNotFoundException {
        return balanceService.getBalanceById(id);
    }

}
