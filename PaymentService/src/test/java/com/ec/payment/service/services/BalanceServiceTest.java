package com.ec.payment.service.services;

import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.payloads.ErrorResponse;
import com.ec.payment.service.payloads.SuccessResponse;
import com.ec.payment.service.repo.BalanceRepository;
import com.ec.payment.service.services.impl.BalanceServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private SuccessResponse successResponse;

    @Mock
    private ErrorResponse errorResponse;

    @InjectMocks
    private BalanceServiceImplementation balanceService;

    // ============================
    // 1. addBalance() Tests
    // ============================

    @Test
    void addBalance_ShouldUpdateExistingWallet() {
        // Data Setup
        UserBalance input = new UserBalance(1L, 500.0);
        UserBalance existingWallet = new UserBalance(1L, 1000.0);

        when(balanceRepository.findById(1L)).thenReturn(Optional.of(existingWallet));
        when(balanceRepository.save(any())).thenReturn(existingWallet);
        when(successResponse.responseHandler(anyString(), eq(HttpStatus.OK), any()))
                .thenReturn(ResponseEntity.ok("Success"));

        // Execution
        ResponseEntity<?> response = balanceService.addBalance(input);

        // Verification
        verify(balanceRepository).save(argThat(wallet -> wallet.getAmount() == 1500.0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addBalance_ShouldCreateNewWalletIfNotFound() {
        UserBalance input = new UserBalance(2L, 200.0);

        when(balanceRepository.findById(2L)).thenReturn(Optional.empty());
        when(successResponse.responseHandler(anyString(), eq(HttpStatus.OK), any()))
                .thenReturn(ResponseEntity.ok("Success"));

        balanceService.addBalance(input);

        // Verify that it starts with 0.0 and adds 200.0
        verify(balanceRepository).save(argThat(wallet -> wallet.getAmount() == 200.0));
    }

    // ============================
    // 2. getBalanceById() Tests
    // ============================

    @Test
    void getBalanceById_ShouldReturnWallet() throws ResourceNotFoundException {
        UserBalance wallet = new UserBalance(1L, 500.0);
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok(wallet));

        ResponseEntity<?> response = balanceService.getBalanceById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBalanceById_ShouldThrowExceptionWhenNotFound() {
        when(balanceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> balanceService.getBalanceById(1L));
    }

    // ============================
    // 3. getAllBalance() Tests
    // ============================

    @Test
    void getAllBalance_ShouldReturnList() {
        List<UserBalance> wallets = List.of(new UserBalance(1L, 100.0), new UserBalance(2L, 200.0));
        when(balanceRepository.findAll()).thenReturn(wallets);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok(wallets));

        ResponseEntity<?> response = balanceService.getAllBalance();

        verify(balanceRepository).findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}