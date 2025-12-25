package com.ec.inventory.service.services;

import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {
    public ResponseEntity<?> addInventory(Inventory inventory);
    public ResponseEntity<?> getInventoryById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> getAllInventory();
    public ResponseEntity<?> updateInventory(Long id, Inventory updatedInventory) throws ResourceNotFoundException;
    public ResponseEntity<?> deleteInventory(Long id) throws ResourceNotFoundException;

    // Batch process
    public List<Inventory> getInventoryByProductIds(List<Long> productIds);
}
