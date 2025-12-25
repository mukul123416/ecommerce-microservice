package com.ec.product.service.controllers;
import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.product.service.services.ProductService;
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
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(
            summary = "Add product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"Product add successfully\",\n" +
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
    public ResponseEntity<?> add(@Valid @RequestBody Product product){
        return productService.addProduct(product);
    }

    @Operation(
            summary = "Get all product",
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
        return productService.getAllProducts();
    }

    @Operation(
            summary = "Get product by ID",
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
        return productService.getProductById(id);
    }

    @Operation(
            summary = "Update product by ID",
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product) throws ResourceNotFoundException {
        return productService.updateProduct(id, product);
    }

    @Operation(
            summary = "Delete product by ID",
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
        return productService.deleteProduct(id);
    }

    // ✅ Batch endpoint
    @GetMapping("/batch")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestParam List<Long> ids) {
        List<Product> products = productService.getProductsByIds(ids);
        return ResponseEntity.ok(products);
    }

}
