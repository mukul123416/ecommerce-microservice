package com.ec.user.service.services;

import com.ec.user.service.entities.User;
import com.ec.user.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.user.service.payloads.Order;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> placeOrder(Order order);
    public ResponseEntity<?> createUser(User user);
    public ResponseEntity<?> getUserById(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> getAllUsers();
    public ResponseEntity<?> updateUser(Long id, User updatedUser) throws ResourceNotFoundException;
    public ResponseEntity<?> deleteUser(Long id) throws ResourceNotFoundException;
    public ResponseEntity<?> fetchUserBasic(Long userId) throws ResourceNotFoundException;
}
