package com.ec.inventory.service.services.Impl;

import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.inventory.service.payloads.ErrorResponse;
import com.ec.inventory.service.payloads.SuccessResponse;
import com.ec.inventory.service.repo.InventoryRepository;
import com.ec.inventory.service.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImplementation implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ErrorResponse errorResponse;
    @Autowired
    private SuccessResponse successResponse;

    @Override
    public ResponseEntity<?> addInventory(Inventory inventory)  {
        ResponseEntity<?> response;
        try {
            inventoryRepository.save(inventory);
            response = successResponse.responseHandler("Inventory add successfully", HttpStatus.OK,null);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> getInventoryById(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found with id : "+id));
        response = successResponse.responseHandler("Fetched Successful",HttpStatus.OK,inventory);
            return response;
    }

    @Override
    public ResponseEntity<?> getAllInventory() {
        ResponseEntity<?> response;
        try {
            List<Inventory> inventories = inventoryRepository.findAll();
            response = successResponse.responseHandler("Fetched Successful", HttpStatus.OK,inventories);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> updateInventory(Long id, Inventory updatedInventory) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found with id "+id));
            inventory.setProductId(updatedInventory.getProductId());
            inventory.setQuantity(updatedInventory.getQuantity());
            inventoryRepository.save(inventory);
            response = successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<?> deleteInventory(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found with id "+id));
            inventoryRepository.deleteById(inventory.getId());
            response = successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public List<Inventory> getInventoryByProductIds(List<Long> productIds) {
        return inventoryRepository.findByProductIdIn(productIds);
    }
}
