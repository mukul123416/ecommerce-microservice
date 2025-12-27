package com.ec.order.service.controllers;

import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId(2L);
        order.setUserId(1L);
        order.setProductId(2L);
        order.setQuantity(2);
        order.setStatus("CREATED");
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> response(String body) {
        return (ResponseEntity<T>) new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Test
    void testAddOrder() {
        when(orderService.placeOrder(any(Order.class)))
                .thenReturn(response("Order placed, processing..."));

        ResponseEntity<?> response = orderController.placeOrder(order);

        assertEquals(200, response.getStatusCode().value());
        verify(orderService, times(1)).placeOrder(order);
    }

    @Test
    void testGetAllOrder() {
        when(orderService.getAllOrders()).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = orderController.getAll();

        assertEquals(200, response.getStatusCode().value());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById() throws ResourceNotFoundException {
        when(orderService.getOrderById(1L)).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = orderController.getById(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testUpdateOrder() throws ResourceNotFoundException {
        when(orderService.updateOrder(eq(1L), any(Order.class)))
                .thenReturn(response("Update Successful"));

        ResponseEntity<?> response = orderController.update(1L, order);

        assertEquals(200, response.getStatusCode().value());
        verify(orderService, times(1)).updateOrder(1L, order);
    }

    @Test
    void testDeleteOrder() throws ResourceNotFoundException {
        when(orderService.deleteOrder(1L)).thenReturn(response("Delete Successful"));

        ResponseEntity<?> response = orderController.delete(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void testGetOrdersByUserId() throws ResourceNotFoundException {
        Long userId = 1L;
        when(orderService.getOrdersByUserId(userId))
                .thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = orderController.getOrderByUserId(userId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        verify(orderService, times(1)).getOrdersByUserId(userId);
    }

    @Test
    void testGetOrdersByProductId() throws ResourceNotFoundException {
        Long productId = 1L;
        when(orderService.getOrdersByProductId(productId))
                .thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = orderController.getOrderByProductId(productId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        verify(orderService, times(1)).getOrdersByProductId(productId);
    }


}
