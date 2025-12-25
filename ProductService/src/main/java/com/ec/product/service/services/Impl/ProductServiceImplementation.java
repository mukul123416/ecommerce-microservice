package com.ec.product.service.services.Impl;

import com.ec.product.service.dtos.InventoryDTO;
import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.product.service.payloads.ErrorResponse;
import com.ec.product.service.payloads.SuccessResponse;
import com.ec.product.service.repo.ProductRepository;
import com.ec.product.service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ErrorResponse errorResponse;
    @Autowired
    private SuccessResponse successResponse;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> addProduct(Product product)  {
        ResponseEntity<?> response;
        try {
            productRepo.save(product);
            response = successResponse.responseHandler("Product add successfully", HttpStatus.OK,null);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> getProductById(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Product product = productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id : "+id));
        response = successResponse.responseHandler("Fetched Successful",HttpStatus.OK,product);
            return response;
    }

    @Override
    public ResponseEntity<?> getAllProducts() {
        ResponseEntity<?> response;
        try {
            List<Product> productList = productRepo.findAll();
            response = successResponse.responseHandler("Fetched Successful", HttpStatus.OK,productList);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> updateProduct(Long id, Product updatedProduct) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Product product = productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setOrderId(updatedProduct.getOrderId());
            productRepo.save(product);
            response = successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
            Product product = productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));
            productRepo.deleteById(product.getId());
            response = successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids) {
        List<Product> products = productRepo.findAllById(ids);

        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("&productIds="));

        InventoryDTO[] inventoryArray = restTemplate.getForObject(
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
