package com.ec.payment.service.services;

import com.ec.payment.service.entities.Payment;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    public ResponseEntity<?> addPayment(Payment payment);
    public ResponseEntity<?> getPaymentById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> getAllPayment();
    public ResponseEntity<?> updatePayment(Long id, Payment updatedPayment) throws ResourceNotFoundException;
    public ResponseEntity<?> deletePayment(Long id) throws ResourceNotFoundException;

    // batch process
    public List<Payment> getPaymentByOrderIds(List<Long> orderIds);
}
