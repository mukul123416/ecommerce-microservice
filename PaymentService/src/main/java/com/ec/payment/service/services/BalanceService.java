package com.ec.payment.service.services;

import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

public interface BalanceService {

    public ResponseEntity<Object> addBalance(UserBalance userBalance);
    public ResponseEntity<Object> getBalanceById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> getAllBalance();
}
