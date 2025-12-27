package com.ec.inventory.service.services.impl;

import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.inventory.service.payloads.ErrorResponse;
import com.ec.inventory.service.payloads.SuccessResponse;
import com.ec.inventory.service.repo.InventoryRepository;
import com.ec.inventory.service.services.InventoryService;
import com.ec.inventory.service.utilities.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImplementation implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ErrorResponse errorResponse;
    private final SuccessResponse successResponse;

    public InventoryServiceImplementation(InventoryRepository inventoryRepository, ErrorResponse errorResponse, SuccessResponse successResponse) {
        this.inventoryRepository = inventoryRepository;
        this.errorResponse = errorResponse;
        this.successResponse = successResponse;
    }

    @Override
    public ResponseEntity<Object> addInventory(Inventory inventory)  {
        ResponseEntity<Object> response;
        try {
            this.inventoryRepository.save(inventory);
            response = this.successResponse.responseHandler("Inventory add successfully", HttpStatus.OK,null);
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> getInventoryById(Long id) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Inventory inventory = this.inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found with id : "+id));
        response = this.successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY,HttpStatus.OK,inventory);
            return response;
    }

    @Override
    public ResponseEntity<Object> getAllInventory() {
        ResponseEntity<Object> response;
        try {
            List<Inventory> inventories = this.inventoryRepository.findAll();
            response = this.successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK,inventories);
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> updateInventory(Long id, Inventory updatedInventory) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Inventory inventory = this.inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found with id "+id));
            inventory.setProductId(updatedInventory.getProductId());
            inventory.setQuantity(updatedInventory.getQuantity());
            this.inventoryRepository.save(inventory);
            response = this.successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<Object> deleteInventory(Long id) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Inventory inventory = this.inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found with id "+id));
            this.inventoryRepository.deleteById(inventory.getId());
            response = this.successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public List<Inventory> getInventoryByProductIds(List<Long> productIds) {
        return this.inventoryRepository.findByProductIdIn(productIds);
    }
}
