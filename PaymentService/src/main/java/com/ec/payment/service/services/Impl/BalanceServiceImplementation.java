package com.ec.payment.service.services.Impl;

import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.payloads.ErrorResponse;
import com.ec.payment.service.payloads.SuccessResponse;
import com.ec.payment.service.repo.BalanceRepository;
import com.ec.payment.service.services.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceServiceImplementation implements BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private ErrorResponse errorResponse;
    @Autowired
    private SuccessResponse successResponse;
    @Override
    public ResponseEntity<?> addBalance(UserBalance userBalance) {
        ResponseEntity<?> response;
        try {

            if (userBalance.getAmount() == null || userBalance.getAmount() <= 0) {
                return errorResponse.responseHandler(
                        "Amount must be greater than zero",
                        HttpStatus.BAD_REQUEST
                );
            }
            UserBalance wallet = balanceRepository.findById(userBalance.getUserId())
                    .orElse(new UserBalance(userBalance.getUserId(), 0.0));
            double newAmount = Math.round((wallet.getAmount() + userBalance.getAmount()) * 100.0) / 100.0;
            wallet.setAmount(newAmount);
            balanceRepository.save(wallet);
            response = successResponse.responseHandler(
                    "Balance Updated! Total: " + String.format("%.2f", wallet.getAmount()),
                    HttpStatus.OK,
                    null
            );
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> getBalanceById(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        UserBalance userBalance = balanceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Balance not found with id : "+id));
        response = successResponse.responseHandler("Fetched Successful",HttpStatus.OK,userBalance);
        return response;
    }

    @Override
    public ResponseEntity<?> getAllBalance() {
        ResponseEntity<?> response;
        try {
            List<UserBalance> userBalances = balanceRepository.findAll();
            response = successResponse.responseHandler("Fetched Successful", HttpStatus.OK,userBalances);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
