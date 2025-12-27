package com.ec.inventory.service.services;

import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {
    public ResponseEntity<Object> addInventory(Inventory inventory);
    public ResponseEntity<Object> getInventoryById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> getAllInventory();
    public ResponseEntity<Object> updateInventory(Long id, Inventory updatedInventory) throws ResourceNotFoundException;
    public ResponseEntity<Object> deleteInventory(Long id) throws ResourceNotFoundException;

    // Batch process
    public List<Inventory> getInventoryByProductIds(List<Long> productIds);
}
