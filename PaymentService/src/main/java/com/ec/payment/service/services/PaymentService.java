package com.ec.payment.service.services;

import com.ec.payment.service.entities.Payment;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    public ResponseEntity<Object> addPayment(Payment payment);
    public ResponseEntity<Object> getPaymentById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> getAllPayment();
    public ResponseEntity<Object> updatePayment(Long id, Payment updatedPayment) throws ResourceNotFoundException;
    public ResponseEntity<Object> deletePayment(Long id) throws ResourceNotFoundException;

    // batch process
    public List<Payment> getPaymentByOrderIds(List<Long> orderIds);
}
