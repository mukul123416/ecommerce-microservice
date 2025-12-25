package com.ec.user.service.services;

import com.ec.user.service.dtos.OrderWithUserIdDTO;
import com.ec.user.service.entities.User;
import com.ec.user.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.user.service.helper.OrderClient;
import com.ec.user.service.payloads.ApiResponse;
import com.ec.user.service.payloads.ErrorResponse;
import com.ec.user.service.payloads.Order;
import com.ec.user.service.payloads.SuccessResponse;
import com.ec.user.service.repo.UserRepository;
import com.ec.user.service.services.Impl.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepo;


    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SuccessResponse successResponse;

    @Mock
    private ErrorResponse errorResponse;

    @InjectMocks
    private UserServiceImplementation userService;

    @Mock
    private OrderClient orderClient;


    // ============================
    // 1. PlaceOrder() tests
    // ============================
    @Test
    void shouldPlaceOrderSuccessfully() {
        Order details = new Order();

        ApiResponse<Object> mockApiResponse = new ApiResponse<>();
        mockApiResponse.setMessage("Order placed, processing...");
        mockApiResponse.setData(new Object());

        when(orderClient.placeOrder(any(Order.class)))
                .thenReturn((ApiResponse) mockApiResponse);

        when(successResponse.responseHandler(anyString(), eq(HttpStatus.OK), any()))
                .thenReturn(new ResponseEntity<>("Order placed, processing...", HttpStatus.OK));

        ResponseEntity<?> response = userService.placeOrder(details);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnErrorWhenOrderServiceUnavailable() {
        Order details = new Order();
        ApiResponse<Object> mockApiResponse = new ApiResponse<>();
        mockApiResponse.setData(null); // Data null matlab service unavailable logic

        when(orderClient.placeOrder(any(Order.class)))
                .thenReturn((ApiResponse) mockApiResponse);
        when(errorResponse.responseHandler(contains("unavailable"), eq(HttpStatus.BAD_REQUEST)))
                .thenReturn(new ResponseEntity<>("Order service unavailable", HttpStatus.BAD_REQUEST));

        ResponseEntity<?> response = userService.placeOrder(details);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // ============================
    // 2. createUser() tests
    // ============================
    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setName("Mukul Sharma");
        user.setPassword("12345");
        user.setAbout("Hey,my name is mukul sharma");
        user.setEmail("ms22289@gmail.com");

        when(passwordEncoder.encode("12345")).thenReturn("encoded12345");
        when(userRepo.save(any())).thenReturn(user);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("User registered successfully"));

        ResponseEntity<?> response = userService.createUser(user);

        verify(userRepo).save(any(User.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // ============================
    // 3. getUserById() tests
    // ============================
    @Test
    void shouldReturnUserById() throws ResourceNotFoundException {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Fetched Successful", HttpStatus.OK));

        ResponseEntity<?> response = userService.getUserById(1L);

        verify(userRepo).findById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowNotFoundWhenUserIdInvalid() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }


    // ============================
    // 4. getAllUsers() tests
    // ============================
    @Test
    void shouldReturnAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        ApiResponse<List<OrderWithUserIdDTO>> mockApiResponse = new ApiResponse<>();
        mockApiResponse.setData(Collections.emptyList());

        when(userRepo.findAll()).thenReturn(List.of(user1, user2));

        when(orderClient.getAll()).thenReturn(mockApiResponse);

        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = userService.getAllUsers();

        verify(userRepo).findAll();
        verify(orderClient).getAll();
        assertEquals(200, response.getStatusCode().value());
    }


    // ============================
    // 5. updateUser() tests
    // ============================
    @Test
    void shouldUpdateUserSuccessfully() throws ResourceNotFoundException {
        User existingUser = new User();
        existingUser.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(any())).thenReturn(existingUser);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Update Successful", HttpStatus.OK));

        User updateUser = new User();
        updateUser.setName("Updated");

        ResponseEntity<?> response = userService.updateUser(1L, updateUser);

        verify(userRepo).save(existingUser);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, new User()));
    }


    // ============================
    // 6. deleteUser() tests
    // ============================
    @Test
    void shouldDeleteUserSuccessfully() throws ResourceNotFoundException {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepo).deleteById(1L);
        when(successResponse.responseHandler(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>("Delete Successful", HttpStatus.OK));

        ResponseEntity<?> response = userService.deleteUser(1L);

        verify(userRepo).deleteById(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldThrowExceptionWhenDeleteUserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }

}
