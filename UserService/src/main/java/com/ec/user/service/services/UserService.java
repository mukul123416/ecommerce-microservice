package com.ec.user.service.services;

import com.ec.user.service.entities.User;
import com.ec.user.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.user.service.payloads.Order;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Object> placeOrder(Order order);
    public ResponseEntity<Object> createUser(User user);
    public ResponseEntity<Object> getUserById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> getAllUsers();
    public ResponseEntity<Object> updateUser(Long id, User updatedUser) throws ResourceNotFoundException;
    public ResponseEntity<Object> deleteUser(Long id) throws ResourceNotFoundException;
    public ResponseEntity<Object> fetchUserBasic(Long userId) throws ResourceNotFoundException;
}
