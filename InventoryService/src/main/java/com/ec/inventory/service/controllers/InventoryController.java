package com.ec.inventory.service.controllers;
import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.inventory.service.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(
            summary = "Add inventory",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "Inventory add successfully",
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
    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody Inventory inventory){
        return this.inventoryService.addInventory(inventory);
    }

    @Operation(
            summary = "Get all inventory",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "Fetched successfully",
                                      "error": false,
                                      "status": 200,
                                      "data": null
                                    }
                                    """)
                    ))
            }
    )
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return this.inventoryService.getAllInventory();
    }

    @Operation(
            summary = "Get inventory by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "Fetched successfully",
                                      "error": false,
                                      "status": 200,
                                      "data": null
                                    }
                                    """)
                    ))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return this.inventoryService.getInventoryById(id);
    }

    @Operation(summary = "Update inventory by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Inventory inventory) throws ResourceNotFoundException {
        return this.inventoryService.updateInventory(id, inventory);
    }

    @Operation(summary = "Delete inventory by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws ResourceNotFoundException {
        return this.inventoryService.deleteInventory(id);
    }

    // ✅ Batch endpoint
    @GetMapping("/batch")
    public ResponseEntity<List<Inventory>> getInventoryByProductIds(@RequestParam List<Long> productIds) {
        List<Inventory> inventories = this.inventoryService.getInventoryByProductIds(productIds);
        return ResponseEntity.ok(inventories);
    }

}
