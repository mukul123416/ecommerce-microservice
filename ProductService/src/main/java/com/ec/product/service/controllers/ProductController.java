package com.ec.product.service.controllers;

import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.product.service.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Add product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "Product add successfully",
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
    public ResponseEntity<Object> add(@Valid @RequestBody Product product) {
        return this.productService.addProduct(product);
    }

    @Operation(
            summary = "Get all product",
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
        return this.productService.getAllProducts();
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return this.productService.getProductById(id);
    }

    @Operation(summary = "Update product by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Product product) throws ResourceNotFoundException {
        return this.productService.updateProduct(id, product);
    }

    @Operation(summary = "Delete product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws ResourceNotFoundException {
        return this.productService.deleteProduct(id);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestParam List<Long> ids) {
        List<Product> products = this.productService.getProductsByIds(ids);
        return ResponseEntity.ok(products);
    }
}