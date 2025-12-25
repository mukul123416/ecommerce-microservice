package com.ec.order.service.services;

import com.ec.order.service.dtos.PaymentDTO;
import com.ec.order.service.dtos.ProductDTO;
import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.payloads.ErrorResponse;
import com.ec.order.service.payloads.SuccessResponse;
import com.ec.order.service.repo.OrderRepository;
import com.ec.order.service.services.Impl.OrderServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepo;

    @Mock
    private SuccessResponse successResponse;

    @InjectMocks
    private OrderServiceImplementation orderService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ErrorResponse errorResponse;

    @Mock
    private KafKaProducerService kafKaProducerService;


    // ============================
    // 1. addOrder() tests
    // ============================
    @Test
    void shouldAddOrderSuccessfully() throws ExecutionException, InterruptedException {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setProductId(2L);
        order.setQuantity(2);

        when(orderRepo.save(any())).thenReturn(order);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Order placed, processing..."));

        ResponseEntity<?> response = orderService.placeOrder(order);

        verify(orderRepo).save(any(Order.class));
        verify(kafKaProducerService).sendMessage(anyString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // ============================
    // 2. getOrderById() tests
    // ============================
    @Test
    void shouldReturnOrderById() throws ResourceNotFoundException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = orderService.getOrderById(1L);

        verify(orderRepo).findById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowNotFoundWhenOrderIdInvalid() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }


    // ============================
    // 3. getAllOrders() tests
    // ============================
    @Test
    void shouldReturnAllOrders() {
        Order order1 = new Order();
        Order order2 = new Order();
        when(orderRepo.findAll()).thenReturn(List.of(order1, order2));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = orderService.getAllOrders();

        verify(orderRepo).findAll();
        assertEquals(200, response.getStatusCode().value());
    }


    // ============================
    // 4. updateOrder() tests
    // ============================
    @Test
    void shouldUpdateOrderSuccessfully() throws ResourceNotFoundException {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepo.save(any())).thenReturn(existingOrder);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Update Successful", HttpStatus.OK));

        Order updateOrder = new Order();
        updateOrder.setQuantity(5);

        ResponseEntity<?> response = orderService.updateOrder(1L, updateOrder);

        verify(orderRepo).save(existingOrder);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenUpdateOrderNotFound() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(1L, new Order()));
    }


    // ============================
    // 5. deleteOrder() tests
    // ============================
    @Test
    void shouldDeleteOrderSuccessfully() throws ResourceNotFoundException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepo).deleteById(1L);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Delete Successful", HttpStatus.OK));

        ResponseEntity<?> response = orderService.deleteOrder(1L);

        verify(orderRepo).deleteById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenDeleteOrderNotFound() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(1L));
    }

    // ============================
    // 6. getOrdersByUserId() tests
    // ============================
    @Test
    void shouldReturnOrdersByUserIdSuccessfully() throws ResourceNotFoundException {
        Long userId = 1L;

        // Orders
        Order order1 = new Order();
        order1.setId(101L);
        order1.setUserId(userId);
        order1.setProductId(11L);

        Order order2 = new Order();
        order2.setId(102L);
        order2.setUserId(userId);
        order2.setProductId(12L);

        List<Order> orders = List.of(order1, order2);

        // Products
        ProductDTO product1 = new ProductDTO();
        product1.setId(11L);

        ProductDTO product2 = new ProductDTO();
        product2.setId(12L);

        ProductDTO[] productArray = { product1, product2 };

        // Payments
        PaymentDTO payment1 = new PaymentDTO();
        payment1.setOrderId(101L);

        PaymentDTO payment2 = new PaymentDTO();
        payment2.setOrderId(102L);

        PaymentDTO[] paymentArray = { payment1, payment2 };

        // Mock repository
        when(orderRepo.findByUserId(userId)).thenReturn(orders);

        // Mock product service call
        when(restTemplate.getForObject(
                contains("/products/batch"),
                eq(ProductDTO[].class)
        )).thenReturn(productArray);

        // Mock payment service call
        when(restTemplate.getForObject(
                contains("/payments/batch"),
                eq(PaymentDTO[].class)
        )).thenReturn(paymentArray);

        // Mock response handler
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>(orders, HttpStatus.OK));

        // Call service
        ResponseEntity<?> response = orderService.getOrdersByUserId(userId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderRepo).findByUserId(userId);
        verify(restTemplate, times(2)).getForObject(anyString(), any());
    }

    // ============================
    // 7. getOrdersByProductId() tests
    // ============================
    @Test
    void shouldReturnOrdersByProductIdSuccessfully() throws ResourceNotFoundException {
        Long productId = 1L;
        Order order1 = new Order();
        order1.setProductId(productId);
        Order order2 = new Order();
        order2.setProductId(productId);
        List<Order> orders = List.of(order1, order2);
        when(orderRepo.findByProductId(productId)).thenReturn(orders);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = orderService.getOrdersByProductId(productId);

        verify(orderRepo).findByProductId(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
