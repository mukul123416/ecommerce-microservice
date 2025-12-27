package com.ec.product.service.services;

import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    public ResponseEntity<Object> addProduct(Product product);
    public ResponseEntity<Object> getProductById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> getAllProducts();
    public ResponseEntity<Object> updateProduct(Long id, Product updatedProduct) throws ResourceNotFoundException;
    public ResponseEntity<Object> deleteProduct(Long id) throws ResourceNotFoundException;

    // Batch fetch
    public List<Product> getProductsByIds(List<Long> ids);
}
