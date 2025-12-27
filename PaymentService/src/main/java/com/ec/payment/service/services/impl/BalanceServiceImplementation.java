package com.ec.payment.service.services.impl;

import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.payloads.ErrorResponse;
import com.ec.payment.service.payloads.SuccessResponse;
import com.ec.payment.service.repo.BalanceRepository;
import com.ec.payment.service.services.BalanceService;
import com.ec.payment.service.utilities.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceServiceImplementation implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final ErrorResponse errorResponse;
    private final SuccessResponse successResponse;

    public BalanceServiceImplementation(BalanceRepository balanceRepository,
                                        ErrorResponse errorResponse,
                                        SuccessResponse successResponse) {
        this.balanceRepository = balanceRepository;
        this.errorResponse = errorResponse;
        this.successResponse = successResponse;
    }

    @Override
    public ResponseEntity<Object> addBalance(UserBalance userBalance) {
        try {
            if (userBalance.getAmount() == null || userBalance.getAmount() <= 0) {
                return errorResponse.responseHandler(
                        "Amount must be greater than zero",
                        HttpStatus.BAD_REQUEST
                );
            }

            UserBalance wallet = balanceRepository.findById(userBalance.getUserId())
                    .orElse(new UserBalance(userBalance.getUserId(), 0.0));

            // Precision logic
            double newAmount = Math.round((wallet.getAmount() + userBalance.getAmount()) * 100.0) / 100.0;
            wallet.setAmount(newAmount);
            balanceRepository.save(wallet);

            // Using Constant for message
            String successMsg = AppConstants.UPDATE_SUCCESSFUL + "! Total: " + wallet.getAmount();
            return successResponse.responseHandler(successMsg, HttpStatus.OK, null);

        } catch (Exception ex) {
            return errorResponse.responseHandler(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Object> getBalanceById(Long id) throws ResourceNotFoundException {
        UserBalance userBalance = balanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.BALANCE_NOT_FOUND_MESSAGE + id));

        return successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK, userBalance);
    }

    @Override
    public ResponseEntity<Object> getAllBalance() {
        try {
            List<UserBalance> userBalances = balanceRepository.findAll();
            return successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK, userBalances);
        } catch (Exception ex) {
            return errorResponse.responseHandler(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}