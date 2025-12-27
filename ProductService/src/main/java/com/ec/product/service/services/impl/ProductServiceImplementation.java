package com.ec.product.service.services.impl;

import com.ec.product.service.dtos.InventoryDTO;
import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.product.service.payloads.ErrorResponse;
import com.ec.product.service.payloads.SuccessResponse;
import com.ec.product.service.repo.ProductRepository;
import com.ec.product.service.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepo;
    private final ErrorResponse errorResponse;
    private final SuccessResponse successResponse;
    private final RestTemplate restTemplate;

    public ProductServiceImplementation(ProductRepository productRepo, ErrorResponse errorResponse, SuccessResponse successResponse, RestTemplate restTemplate) {
        this.productRepo = productRepo;
        this.errorResponse = errorResponse;
        this.successResponse = successResponse;
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<Object> addProduct(Product product)  {
        ResponseEntity<Object> response;
        try {
            this.productRepo.save(product);
            response = this.successResponse.responseHandler("Product add successfully", HttpStatus.OK,null);
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> getProductById(Long id) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Product product = this.productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id : "+id));
        response = this.successResponse.responseHandler("Fetched Successful",HttpStatus.OK,product);
            return response;
    }

    @Override
    public ResponseEntity<Object> getAllProducts() {
        ResponseEntity<Object> response;
        try {
            List<Product> productList = this.productRepo.findAll();
            response = this.successResponse.responseHandler("Fetched Successful", HttpStatus.OK,productList);
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> updateProduct(Long id, Product updatedProduct) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Product product = this.productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setOrderId(updatedProduct.getOrderId());
            this.productRepo.save(product);
            response = this.successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<Object> deleteProduct(Long id) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
            Product product = this.productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));
            this.productRepo.deleteById(product.getId());
            response = this.successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids) {
        List<Product> products = this.productRepo.findAllById(ids);

        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("&productIds="));

        InventoryDTO[] inventoryArray = this.restTemplate.getForObject(
                "http://localhost:8085/inventory/batch?productIds=" + idsParam,
                InventoryDTO[].class
        );

        Map<Long, InventoryDTO> inventoryMap = Arrays.stream(inventoryArray)
                .collect(Collectors.toMap(InventoryDTO::getProductId, p -> p));

        for (Product product : products) {
            product.setInventory(inventoryMap.get(product.getId()));
        }

        return products;

    }
}
