package com.ec.user.service.services.Impl;

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
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ErrorResponse errorResponse;
    @Autowired
    private SuccessResponse successResponse;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderClient orderClient;

    @Override
    public ResponseEntity<?> placeOrder(Order order)  {
        ResponseEntity<?> response;
        String message;
        try {
            order.setStatus("CREATED");
            ApiResponse<?> apiResponse = orderClient.placeOrder(order);
            if (apiResponse.getData()!=null ) {
                message = apiResponse.getMessage();
                response = successResponse.responseHandler(message, HttpStatus.OK,null);
            } else {
                message = "Order service unavailable";
                response = errorResponse.responseHandler(message,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> createUser(User user)  {
        ResponseEntity<?> response;
        try {
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            userRepo.save(user);
            response = successResponse.responseHandler("User registered successfully", HttpStatus.OK,null);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @RateLimiter(name = "userRateLimiter", fallbackMethod = "rateLimiterFallback")
    @Override
    public ResponseEntity<?> getUserById(Long id) throws ResourceNotFoundException {
        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id)
                );
        List<OrderDTO> orderDTOS = orderClient.getOrdersByUserId(id);
        user.setOrder(orderDTOS);
        String message = "Fetched Successfully";
        if (orderDTOS.isEmpty()) {
            message = "Fetched Successfully (Order service unavailable or no orders)";
        }
        return successResponse.responseHandler(
                message,
                HttpStatus.OK,
                user
        );
    }

    public ResponseEntity<?> rateLimiterFallback(Long userId, RequestNotPermitted ex) {
        return errorResponse.responseHandler(
                "Server is busy, please try again in 10 seconds.",
                HttpStatus.TOO_MANY_REQUESTS
        );
    }
    @Override
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepo.findAll();
        ApiResponse<List<OrderWithUserIdDTO>> all = orderClient.getAll();
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
        String message = "Fetched Successfully";
        if (orders.isEmpty()) {
            message = "Fetched Successfully (Order service unavailable or no orders)";
        }
        return successResponse.responseHandler(
                message,
                HttpStatus.OK,
                users
        );
    }
    @Override
    public ResponseEntity<?> updateUser(Long id, User updatedUser) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setAbout(updatedUser.getAbout());
            userRepo.save(user);
            response = successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
            User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
            userRepo.deleteById(user.getId());
            response = successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public ResponseEntity<?> fetchUserBasic(Long userId) throws ResourceNotFoundException {
        UserDTO userDTO = new UserDTO();
        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + userId)
                );
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAbout(user.getAbout());
        return successResponse.responseHandler(
                "Fetched Successfully",
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
