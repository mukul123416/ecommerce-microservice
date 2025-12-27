package com.ec.inventory.service.services;

import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.inventory.service.payloads.ErrorResponse;
import com.ec.inventory.service.payloads.SuccessResponse;
import com.ec.inventory.service.repo.InventoryRepository;
import com.ec.inventory.service.services.impl.InventoryServiceImplementation;
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
class InventoryServiceTest {
    @Mock
    private InventoryRepository inventoryRepo;

    @Mock
    private SuccessResponse successResponse;

    @Mock
    private ErrorResponse errorResponse;

    @InjectMocks
    private InventoryServiceImplementation inventoryService;


    // ============================
    // 1. addInventory() tests
    // ============================
    @Test
    void shouldAddInventorySuccessfully() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantity(2);

        when(inventoryRepo.save(any())).thenReturn(inventory);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Inventory add successfully"));

        ResponseEntity<?> response = inventoryService.addInventory(inventory);

        verify(inventoryRepo).save(any(Inventory.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // ============================
    // 2. getInventoryById() tests
    // ============================
    @Test
    void shouldReturnInventoryById() throws ResourceNotFoundException {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventory));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = inventoryService.getInventoryById(1L);

        verify(inventoryRepo).findById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowNotFoundWhenInventoryIdInvalid() {
        when(inventoryRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getInventoryById(1L));
    }


    // ============================
    // 3. getAllInventory() tests
    // ============================
    @Test
    void shouldReturnAllInventory() {
        Inventory inventory1 = new Inventory();
        Inventory inventory2 = new Inventory();
        when(inventoryRepo.findAll()).thenReturn(List.of(inventory1, inventory2));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = inventoryService.getAllInventory();

        verify(inventoryRepo).findAll();
        assertEquals(200, response.getStatusCode().value());
    }


    // ============================
    // 4. updateInventory() tests
    // ============================
    @Test
    void shouldUpdateInventorySuccessfully() throws ResourceNotFoundException {
        Inventory existingInventory = new Inventory();
        existingInventory.setId(1L);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepo.save(any())).thenReturn(existingInventory);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Update Successful", HttpStatus.OK));

        Inventory updateInventory = new Inventory();
        updateInventory.setQuantity(5);

        ResponseEntity<?> response = inventoryService.updateInventory(1L, updateInventory);

        verify(inventoryRepo).save(existingInventory);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenUpdateInventoryNotFound() {
        when(inventoryRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.updateInventory(1L, new Inventory()));
    }


    // ============================
    // 5. deleteInventory() tests
    // ============================
    @Test
    void shouldDeleteInventorySuccessfully() throws ResourceNotFoundException {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventory));
        doNothing().when(inventoryRepo).deleteById(1L);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Delete Successful", HttpStatus.OK));

        ResponseEntity<?> response = inventoryService.deleteInventory(1L);

        verify(inventoryRepo).deleteById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenDeleteInventoryNotFound() {
        when(inventoryRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> inventoryService.deleteInventory(1L));
    }

}
