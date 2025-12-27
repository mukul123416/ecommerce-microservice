package com.ec.payment.service.services;

import com.ec.payment.service.entities.Payment;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.payloads.ErrorResponse;
import com.ec.payment.service.payloads.SuccessResponse;
import com.ec.payment.service.repo.PaymentRepository;
import com.ec.payment.service.services.impl.PaymentServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private SuccessResponse successResponse;

    @Mock
    private ErrorResponse errorResponse;

    @InjectMocks
    private PaymentServiceImplementation paymentService;


    // ============================
    // 1. createPayment() tests
    // ============================
    @Test
    void shouldCreatePaymentSuccessfully() {
        Payment payment = new Payment();
        payment.setUserId(1L);
        payment.setOrderId(2L);
        payment.setAmount(3000.99);
        payment.setStatus("SUCCESS");

        when(paymentRepo.save(any())).thenReturn(payment);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Payment add successfully"));

        ResponseEntity<?> response = paymentService.addPayment(payment);

        verify(paymentRepo).save(any(Payment.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // ============================
    // 2. getPaymentById() tests
    // ============================
    @Test
    void shouldReturnPaymentById() throws ResourceNotFoundException {
        Payment payment = new Payment();
        payment.setId(1L);
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = paymentService.getPaymentById(1L);

        verify(paymentRepo).findById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowNotFoundWhenPaymentIdInvalid() {
        when(paymentRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPaymentById(1L));
    }


    // ============================
    // 3. getAllPayment() tests
    // ============================
    @Test
    void shouldReturnAllPayment() {
        Payment payment1 = new Payment();
        Payment payment2 = new Payment();
        when(paymentRepo.findAll()).thenReturn(List.of(payment1, payment2));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = paymentService.getAllPayment();

        verify(paymentRepo).findAll();
        assertEquals(200, response.getStatusCode().value());
    }


    // ============================
    // 4. updatePayment() tests
    // ============================
    @Test
    void shouldUpdatePaymentSuccessfully() throws ResourceNotFoundException {
        Payment existingPayment = new Payment();
        existingPayment.setId(1L);
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(existingPayment));
        when(paymentRepo.save(any())).thenReturn(existingPayment);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Update Successful", HttpStatus.OK));

        Payment updatePayment = new Payment();
        updatePayment.setStatus("FAILED");

        ResponseEntity<?> response = paymentService.updatePayment(1L, updatePayment);

        verify(paymentRepo).save(existingPayment);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenUpdatePaymentNotFound() {
        when(paymentRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.updatePayment(1L, new Payment()));
    }


    // ============================
    // 5. deletePayment() tests
    // ============================
    @Test
    void shouldDeletePaymentSuccessfully() throws ResourceNotFoundException {
        Payment payment = new Payment();
        payment.setId(1L);
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));
        doNothing().when(paymentRepo).deleteById(1L);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Delete Successful", HttpStatus.OK));

        ResponseEntity<?> response = paymentService.deletePayment(1L);

        verify(paymentRepo).deleteById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenDeletePaymentNotFound() {
        when(paymentRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> paymentService.deletePayment(1L));
    }

    // ============================
    // 6. getPaymentByOrderIds() tests
    // ============================
    @Test
    void shouldReturnPaymentsForGivenOrderIds() {
        List<Long> orderIds = List.of(101L, 102L);

        Payment p1 = new Payment();
        p1.setOrderId(101L);
        p1.setAmount(500.0);

        Payment p2 = new Payment();
        p2.setOrderId(102L);
        p2.setAmount(1200.0);

        when(paymentRepo.findByOrderIdIn(orderIds)).thenReturn(List.of(p1, p2));

        List<Payment> result = paymentService.getPaymentByOrderIds(orderIds);

        assertEquals(2, result.size());
        assertEquals(101L, result.get(0).getOrderId());
        assertEquals(102L, result.get(1).getOrderId());

        verify(paymentRepo, times(1)).findByOrderIdIn(orderIds);
    }

}
