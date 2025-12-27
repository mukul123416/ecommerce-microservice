package com.ec.user.service.services.impl;

import com.ec.user.service.dtos.OrderDTO;
import com.ec.user.service.dtos.OrderWithUserIdDTO;
import com.ec.user.service.dtos.UserDTO;
import com.ec.user.service.entities.User;
import com.ec.user.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.user.service.helper.OrderClient;
import com.ec.user.service.payloads.ApiResponse;
import com.ec.user.service.payloads.ErrorResponse;
import com.ec.user.service.payloads.Order;
import com.ec.user.service.payloads.SuccessResponse;
import com.ec.user.service.repo.UserRepository;
import com.ec.user.service.services.UserService;
import com.ec.user.service.utilities.AppConstants;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepo;
    private final ErrorResponse errorResponse;
    private final SuccessResponse successResponse;
    private final PasswordEncoder passwordEncoder;
    private final OrderClient orderClient;

    public UserServiceImplementation(
            UserRepository userRepo,
            ErrorResponse errorResponse,
            SuccessResponse successResponse,
            PasswordEncoder passwordEncoder,
            OrderClient orderClient) {
        this.userRepo = userRepo;
        this.errorResponse = errorResponse;
        this.successResponse = successResponse;
        this.passwordEncoder = passwordEncoder;
        this.orderClient = orderClient;
    }

    @Override
    public ResponseEntity<Object> placeOrder(Order order)  {
        ResponseEntity<Object> response;
        String message;
        try {
            order.setStatus("CREATED");
            ApiResponse<Object> apiResponse = this.orderClient.placeOrder(order);
            if (apiResponse.getData()!=null ) {
                message = apiResponse.getMessage();
                response = this.successResponse.responseHandler(message, HttpStatus.OK,null);
            } else {
                message = "Order service unavailable";
                response = this.errorResponse.responseHandler(message,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> createUser(User user)  {
        ResponseEntity<Object> response;
        try {
            String encodePassword = this.passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            this.userRepo.save(user);
            response = this.successResponse.responseHandler("User registered successfully", HttpStatus.OK,null);
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RateLimiter(name = "userRateLimiter", fallbackMethod = "rateLimiterFallback")
    @Override
    public ResponseEntity<Object> getUserById(Long id) throws ResourceNotFoundException {
        User user = this.userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id)
                );
        List<OrderDTO> orderDTOS = this.orderClient.getOrdersByUserId(id);
        user.setOrder(orderDTOS);
        String message = AppConstants.FETCHED_SUCCESSFULLY;
        if (orderDTOS.isEmpty()) {
            message = "Fetched Successfully (Order service unavailable or no orders)";
        }
        return this.successResponse.responseHandler(
                message,
                HttpStatus.OK,
                user
        );
    }

    public ResponseEntity<Object> rateLimiterFallback(Long userId, RequestNotPermitted ex) {
        log.error("Server is busy for userId {} : {}", userId, ex.getMessage());
        return this.errorResponse.responseHandler(
                "Server is busy, please try again in 10 seconds.",
                HttpStatus.TOO_MANY_REQUESTS
        );
    }
    @Override
    public ResponseEntity<Object> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        ApiResponse<List<OrderWithUserIdDTO>> all = this.orderClient.getAll();
        List<OrderWithUserIdDTO> orders = all.getData();
        Map<Long, List<OrderWithUserIdDTO>> ordersByUserId =
                orders.stream()
                        .collect(Collectors.groupingBy(OrderWithUserIdDTO::getUserId));
        for (User user : users) {
            List<OrderDTO> finalOrders = ordersByUserId
                    .getOrDefault(user.getId(), Collections.emptyList())
                    .stream()
                    .map(this::toOrderDTO)
                    .collect(Collectors.toList());
            user.setOrder(finalOrders);
        }
        String message = AppConstants.FETCHED_SUCCESSFULLY;
        if (orders.isEmpty()) {
            message = "Fetched Successfully (Order service unavailable or no orders)";
        }
        return this.successResponse.responseHandler(
                message,
                HttpStatus.OK,
                users
        );
    }
    @Override
    public ResponseEntity<Object> updateUser(Long id, User updatedUser) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        User user = this.userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setAbout(updatedUser.getAbout());
            this.userRepo.save(user);
            response = this.successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<Object> deleteUser(Long id) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
            User user = this.userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
            this.userRepo.deleteById(user.getId());
            response = this.successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public ResponseEntity<Object> fetchUserBasic(Long userId) throws ResourceNotFoundException {
        UserDTO userDTO = new UserDTO();
        User user = this.userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + userId)
                );
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAbout(user.getAbout());
        return this.successResponse.responseHandler(
                AppConstants.FETCHED_SUCCESSFULLY,
                HttpStatus.OK,
                userDTO
        );
    }

    private OrderDTO toOrderDTO(OrderWithUserIdDTO o) {
        OrderDTO dto = new OrderDTO();
        dto.setId(o.getId());
        dto.setQuantity(o.getQuantity());
        dto.setOrderDate(o.getOrderDate());
        dto.setProduct(o.getProduct());
        dto.setPayment(o.getPayment());
        dto.setStatus(o.getStatus());
        return dto;
    }
}
