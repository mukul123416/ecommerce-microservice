package com.ec.inventory.service.controllers;

import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.inventory.service.services.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventory = new Inventory();
        inventory.setId(2L);
        inventory.setProductId(1L);
        inventory.setQuantity(2);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> response(String body) {
        return (ResponseEntity<T>) new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Test
    void testAddInventory() {
        when(inventoryService.addInventory(any(Inventory.class)))
                .thenReturn(response("Inventory add successfully"));

        ResponseEntity<?> response = inventoryController.add(inventory);

        assertEquals(200, response.getStatusCode().value());
        verify(inventoryService, times(1)).addInventory(inventory);
    }

    @Test
    void testGetAllInventory() {
        List<Inventory> inventories = Arrays.asList(inventory);
        when(inventoryService.getAllInventory()).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = inventoryController.getAll();

        assertEquals(200, response.getStatusCode().value());
        verify(inventoryService, times(1)).getAllInventory();
    }

    @Test
    void testGetInventoryById() throws ResourceNotFoundException {
        when(inventoryService.getInventoryById(1L)).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = inventoryController.getById(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(inventoryService, times(1)).getInventoryById(1L);
    }

    @Test
    void testUpdateInventory() throws ResourceNotFoundException {
        when(inventoryService.updateInventory(eq(1L), any(Inventory.class)))
                .thenReturn(response("Update Successful"));

        ResponseEntity<?> response = inventoryController.update(1L, inventory);

        assertEquals(200, response.getStatusCode().value());
        verify(inventoryService, times(1)).updateInventory(1L, inventory);
    }

    @Test
    void testDeleteOrder() throws ResourceNotFoundException {
        when(inventoryService.deleteInventory(1L)).thenReturn(response("Delete Successful"));

        ResponseEntity<?> response = inventoryController.delete(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(inventoryService, times(1)).deleteInventory(1L);
    }

}
