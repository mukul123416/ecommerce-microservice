package com.ec.payment.service.services.impl;

import com.ec.payment.service.entities.Payment;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.payloads.ErrorResponse;
import com.ec.payment.service.payloads.SuccessResponse;
import com.ec.payment.service.repo.PaymentRepository;
import com.ec.payment.service.services.PaymentService;
import com.ec.payment.service.utilities.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImplementation implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final ErrorResponse errorResponse;
    private final SuccessResponse successResponse;

    public PaymentServiceImplementation(
            PaymentRepository paymentRepo,
            ErrorResponse errorResponse,
            SuccessResponse successResponse) {
        this.paymentRepo = paymentRepo;
        this.errorResponse = errorResponse;
        this.successResponse = successResponse;
    }

    @Override
    public ResponseEntity<Object> addPayment(Payment payment)  {
        try {
            paymentRepo.save(payment);
            return successResponse.responseHandler("Payment add successfully", HttpStatus.OK, null);
        } catch (Exception ex) {
            return errorResponse.responseHandler(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Object> getPaymentById(Long id) throws ResourceNotFoundException {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND_MESSAGE + id));

        return successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK, payment);
    }

    @Override
    public ResponseEntity<Object> getAllPayment() {
        try {
            List<Payment> payments = paymentRepo.findAll();
            return successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK, payments);
        } catch (Exception ex) {
            return errorResponse.responseHandler(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Object> updatePayment(Long id, Payment updatedPayment) throws ResourceNotFoundException {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND_MESSAGE + id));

        payment.setUserId(updatedPayment.getUserId());
        payment.setOrderId(updatedPayment.getOrderId());
        payment.setAmount(updatedPayment.getAmount());
        payment.setStatus(updatedPayment.getStatus());
        paymentRepo.save(payment);

        return successResponse.responseHandler(AppConstants.UPDATE_SUCCESSFUL, HttpStatus.OK, null);
    }

    @Override
    public ResponseEntity<Object> deletePayment(Long id) throws ResourceNotFoundException {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND_MESSAGE + id));

        paymentRepo.deleteById(payment.getId());
        return successResponse.responseHandler(AppConstants.DELETE_SUCCESSFUL, HttpStatus.OK, null);
    }

    @Override
    public List<Payment> getPaymentByOrderIds(List<Long> orderIds) {
        return paymentRepo.findByOrderIdIn(orderIds);
    }
}