package com.ec.inventory.service.controllers;
import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.inventory.service.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @Operation(
            summary = "Add inventory",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Inventory add successfully\",\n" +
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
    public ResponseEntity<?> add(@Valid @RequestBody Inventory inventory){
        return inventoryService.addInventory(inventory);
    }

    @Operation(
            summary = "Get all inventory",
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
        return inventoryService.getAllInventory();
    }

    @Operation(
            summary = "Get inventory by ID",
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
        return inventoryService.getInventoryById(id);
    }

    @Operation(
            summary = "Update inventory by ID",
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Inventory inventory) throws ResourceNotFoundException {
        return inventoryService.updateInventory(id, inventory);
    }

    @Operation(
            summary = "Delete inventory by ID",
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
        return inventoryService.deleteInventory(id);
    }

    // ✅ Batch endpoint
    @GetMapping("/batch")
    public ResponseEntity<List<Inventory>> getInventoryByProductIds(@RequestParam List<Long> productIds) {
        List<Inventory> inventories = inventoryService.getInventoryByProductIds(productIds);
        return ResponseEntity.ok(inventories);
    }

}
