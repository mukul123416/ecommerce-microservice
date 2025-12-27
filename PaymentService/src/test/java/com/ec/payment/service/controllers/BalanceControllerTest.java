package com.ec.payment.service.controllers;

import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.services.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private PaymentController paymentController;

    private UserBalance userBalance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Test data setup
        userBalance = new UserBalance();
        userBalance.setUserId(1L);
        userBalance.setAmount(500.0);
    }

    // Utility method to simulate service responses
    private <T> ResponseEntity<T> response(String body) {
        return (ResponseEntity<T>) new ResponseEntity<>(body, HttpStatus.OK);
    }

    // ============================
    // 1. addBalance() Test
    // ============================
    @Test
    void testAddBalance() {
        when(balanceService.addBalance(any(UserBalance.class)))
                .thenReturn(response("Balance Updated! Total: 500.0"));

        ResponseEntity<?> response = paymentController.addBalance(userBalance);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(balanceService, times(1)).addBalance(userBalance);
    }

    // ============================
    // 2. getAllBalance() Test
    // ============================
    @Test
    void testGetAllBalance() {
        when(balanceService.getAllBalance())
                .thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = paymentController.getAllBalance();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(balanceService, times(1)).getAllBalance();
    }

    // ============================
    // 3. getBalanceById() Test
    // ============================
    @Test
    void testGetBalanceById() throws ResourceNotFoundException {
        when(balanceService.getBalanceById(1L))
                .thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = paymentController.getBalanceById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(balanceService, times(1)).getBalanceById(1L);
    }
}
