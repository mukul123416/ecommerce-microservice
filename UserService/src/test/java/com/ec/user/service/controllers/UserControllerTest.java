package com.ec.user.service.controllers;

import com.ec.user.service.entities.User;
import com.ec.user.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.user.service.payloads.Order;
import com.ec.user.service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("Mukul Sharma");
        user.setPassword("12345");
        user.setAbout("Hey,my name is mukul sharma");
        user.setEmail("ms22289@gmail.com");
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> response(String body) {
        return (ResponseEntity<T>) new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Test
    void testPlaceOrder() {
        Order details = new Order();
        when(userService.placeOrder(any(Order.class)))
                .thenReturn(response("Order placed, processing..."));

        ResponseEntity<?> response = userController.placeOrder(details);

        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).placeOrder(details);
    }

    @Test
    void testRegisterUser() {
        when(userService.createUser(any(User.class)))
                .thenReturn(response("User registered successfully"));

        ResponseEntity<?> response = userController.register(user);

        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = userController.getAll();

        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() throws ResourceNotFoundException {
        when(userService.getUserById(1L)).thenReturn(response("Fetched Successful"));

        ResponseEntity<?> response = userController.getById(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testUpdateUser() throws ResourceNotFoundException {
        when(userService.updateUser(eq(1L), any(User.class)))
                .thenReturn(response("Update Successful"));

        ResponseEntity<?> response = userController.update(1L, user);

        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    void testDeleteUser() throws ResourceNotFoundException {
        when(userService.deleteUser(1L)).thenReturn(response("Delete Successful"));

        ResponseEntity<?> response = userController.delete(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).deleteUser(1L);
    }

}
