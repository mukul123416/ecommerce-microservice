package com.ec.product.service.services;

import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    public ResponseEntity<?> addProduct(Product product);
    public ResponseEntity<?> getProductById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> getAllProducts();
    public ResponseEntity<?> updateProduct(Long id, Product updatedProduct) throws ResourceNotFoundException;
    public ResponseEntity<?> deleteProduct(Long id) throws ResourceNotFoundException;

    // Batch fetch
    public List<Product> getProductsByIds(List<Long> ids);
}
