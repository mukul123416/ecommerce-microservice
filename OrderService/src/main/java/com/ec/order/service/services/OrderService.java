package com.ec.order.service.services;

import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public ResponseEntity<Object> placeOrder(Order order);
    public ResponseEntity<Object> getOrderById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> getAllOrders();
    public ResponseEntity<Object> updateOrder(Long id, Order updatedOrder) throws ResourceNotFoundException;
    public ResponseEntity<Object> deleteOrder(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> getOrdersByUserId(Long userId) throws ResourceNotFoundException;
    public ResponseEntity<Object> getOrdersByProductId(Long productId) throws ResourceNotFoundException;
}
