package com.ec.product.service.services;

import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.product.service.payloads.ErrorResponse;
import com.ec.product.service.payloads.SuccessResponse;
import com.ec.product.service.repo.ProductRepository;
import com.ec.product.service.services.Impl.ProductServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepo;

    @Mock
    private SuccessResponse successResponse;

    @Mock
    private ErrorResponse errorResponse;

    @InjectMocks
    private ProductServiceImplementation productService;


    // ============================
    // 1. addProduct() tests
    // ============================
    @Test
    void shouldAddProductSuccessfully() {
        Product product = new Product();
        product.setName("Minimalist Gold-Plated Necklace");
        product.setOrderId(1L);
        product.setDescription("Description for the product");
        product.setPrice(2898.99);

        when(productRepo.save(any())).thenReturn(product);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Product add successfully"));

        ResponseEntity<?> response = productService.addProduct(product);

        verify(productRepo).save(any(Product.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // ============================
    // 2. getProductById() tests
    // ============================
    @Test
    void shouldReturnUserById() throws ResourceNotFoundException {
        Product product = new Product();
        product.setId(1L);
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = productService.getProductById(1L);

        verify(productRepo).findById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowNotFoundWhenProductIdInvalid() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }


    // ============================
    // 3. getAllProducts() tests
    // ============================
    @Test
    void shouldReturnAllProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        when(productRepo.findAll()).thenReturn(List.of(product1, product2));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = productService.getAllProducts();

        verify(productRepo).findAll();
        assertEquals(200, response.getStatusCode().value());
    }


    // ============================
    // 4. updateProduct() tests
    // ============================
    @Test
    void shouldUpdateProductSuccessfully() throws ResourceNotFoundException {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        when(productRepo.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepo.save(any())).thenReturn(existingProduct);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Update Successful", HttpStatus.OK));

        Product updateProduct = new Product();
        updateProduct.setName("Updated");

        ResponseEntity<?> response = productService.updateProduct(1L, updateProduct);

        verify(productRepo).save(existingProduct);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenUpdateProductNotFound() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, new Product()));
    }


    // ============================
    // 5. deleteProduct() tests
    // ============================
    @Test
    void shouldDeleteProductSuccessfully() throws ResourceNotFoundException {
        Product product = new Product();
        product.setId(1L);
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepo).deleteById(1L);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Delete Successful", HttpStatus.OK));

        ResponseEntity<?> response = productService.deleteProduct(1L);

        verify(productRepo).deleteById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenDeleteProductNotFound() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
    }

}
