package com.ec.payment.service.services.Impl;

import com.ec.payment.service.entities.Payment;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.payloads.ErrorResponse;
import com.ec.payment.service.payloads.SuccessResponse;
import com.ec.payment.service.repo.PaymentRepository;
import com.ec.payment.service.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImplementation implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private ErrorResponse errorResponse;
    @Autowired
    private SuccessResponse successResponse;

    @Override
    public ResponseEntity<?> addPayment(Payment payment)  {
        ResponseEntity<?> response;
        try {
            paymentRepo.save(payment);
            response = successResponse.responseHandler("Payment add successfully", HttpStatus.OK,null);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> getPaymentById(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Payment payment = paymentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Payment not found with id : "+id));
        response = successResponse.responseHandler("Fetched Successful",HttpStatus.OK,payment);
            return response;
    }

    @Override
    public ResponseEntity<?> getAllPayment() {
        ResponseEntity<?> response;
        try {
            List<Payment> payments = paymentRepo.findAll();
            response = successResponse.responseHandler("Fetched Successful", HttpStatus.OK,payments);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> updatePayment(Long id, Payment updatedPayment) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Payment payment = paymentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Payment not found with id "+id));
            payment.setUserId(updatedPayment.getUserId());
            payment.setOrderId(updatedPayment.getOrderId());
            payment.setAmount(updatedPayment.getAmount());
            payment.setStatus(updatedPayment.getStatus());
            paymentRepo.save(payment);
            response = successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<?> deletePayment(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Payment payment = paymentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Payment not found with id "+id));
            paymentRepo.deleteById(payment.getId());
            response = successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public List<Payment> getPaymentByOrderIds(List<Long> orderIds) {
        return paymentRepo.findByOrderIdIn(orderIds);
    }
}
