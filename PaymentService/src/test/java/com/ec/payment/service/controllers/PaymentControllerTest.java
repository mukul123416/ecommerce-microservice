package com.ec.payment.service.controllers;

import com.ec.payment.service.entities.Payment;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payment = new Payment();
        payment.setId(2L);
        payment.setUserId(1L);
        payment.setOrderId(2L);
        payment.setAmount(3000.99);
        payment.setStatus("SUCCESS");
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> response(String body) {
        return (ResponseEntity<T>) new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Test
    void testAddPayment() {
        when(paymentService.addPayment(any(Payment.class)))
                .thenReturn(response("Payment add successfully"));

        ResponseEntity<?> response = paymentController.add(payment);

        assertEquals(200, response.getStatusCode().value());
        verify(paymentService, times(1)).addPayment(payment);
    }

    @Test
    void testGetAllPayment() {
        when(paymentService.getAllPayment()).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = paymentController.getAll();

        assertEquals(200, response.getStatusCode().value());
        verify(paymentService, times(1)).getAllPayment();
    }

    @Test
    void testGetPaymentById() throws ResourceNotFoundException {
        when(paymentService.getPaymentById(1L)).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = paymentController.getById(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(paymentService, times(1)).getPaymentById(1L);
    }

    @Test
    void testUpdatePayment() throws ResourceNotFoundException {
        when(paymentService.updatePayment(eq(1L), any(Payment.class)))
                .thenReturn(response("Update Successful"));

        ResponseEntity<?> response = paymentController.update(1L, payment);

        assertEquals(200, response.getStatusCode().value());
        verify(paymentService, times(1)).updatePayment(1L, payment);
    }

    @Test
    void testDeletePayment() throws ResourceNotFoundException {
        when(paymentService.deletePayment(1L)).thenReturn(response("Delete Successful"));

        ResponseEntity<?> response = paymentController.delete(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(paymentService, times(1)).deletePayment(1L);
    }

    @Test
    void testGetPaymentByOrderIds() {
        List<Long> orderIds = Arrays.asList(101L, 102L);
        Payment p1 = new Payment();
        p1.setOrderId(101L);
        Payment p2 = new Payment();
        p2.setOrderId(102L);

        List<Payment> mockPayments = Arrays.asList(p1, p2);

        when(paymentService.getPaymentByOrderIds(orderIds)).thenReturn(mockPayments);

        ResponseEntity<List<Payment>> response = paymentController.getPaymentByOrderIds(orderIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(101L, response.getBody().get(0).getOrderId());

        verify(paymentService, times(1)).getPaymentByOrderIds(orderIds);
    }

}
