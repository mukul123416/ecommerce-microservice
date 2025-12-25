package com.ec.order.service.services;

import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public ResponseEntity<?> placeOrder(Order order);
    public ResponseEntity<?> getOrderById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> getAllOrders();
    public ResponseEntity<?> updateOrder(Long id, Order updatedOrder) throws ResourceNotFoundException;
    public ResponseEntity<?> deleteOrder(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> getOrdersByUserId(Long userId) throws ResourceNotFoundException;
    public ResponseEntity<?> getOrdersByProductId(Long productId) throws ResourceNotFoundException;
}
