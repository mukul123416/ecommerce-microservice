package com.ec.product.service.controllers;

import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.product.service.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(2L);
        product.setName("Minimalist Gold-Plated Necklace");
        product.setOrderId(1L);
        product.setDescription("Description for the product");
        product.setPrice(2898.99);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> response(String body) {
        return (ResponseEntity<T>) new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Test
    void testAddProduct() {
        when(productService.addProduct(any(Product.class)))
                .thenReturn(response("Product add successfully"));

        ResponseEntity<?> response = productController.add(product);

        assertEquals(200, response.getStatusCode().value());
        verify(productService, times(1)).addProduct(product);
    }

    @Test
    void testGetAllProduct() {
        when(productService.getAllProducts()).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = productController.getAll();

        assertEquals(200, response.getStatusCode().value());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws ResourceNotFoundException {
        when(productService.getProductById(1L)).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = productController.getById(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testUpdateProduct() throws ResourceNotFoundException {
        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(response("Update Successful"));

        ResponseEntity<?> response = productController.update(1L, product);

        assertEquals(200, response.getStatusCode().value());
        verify(productService, times(1)).updateProduct(1L, product);
    }

    @Test
    void testDeleteProduct() throws ResourceNotFoundException {
        when(productService.deleteProduct(1L)).thenReturn(response("Delete Successful"));

        ResponseEntity<?> response = productController.delete(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(productService, times(1)).deleteProduct(1L);
    }

}
