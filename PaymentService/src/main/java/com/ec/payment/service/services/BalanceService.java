package com.ec.payment.service.services;

import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

public interface BalanceService {

    public ResponseEntity<?> addBalance(UserBalance userBalance);
    public ResponseEntity<?> getBalanceById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> getAllBalance();
}
