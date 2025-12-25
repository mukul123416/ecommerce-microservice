package com.ec.user.service.helper;

import com.ec.user.service.dtos.OrderDTO;
import com.ec.user.service.dtos.OrderWithUserIdDTO;
import com.ec.user.service.payloads.ApiResponse;
import com.ec.user.service.payloads.Order;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class OrderClient {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OrderService orderService;

    @Retry(name = "orderServiceByUserId",fallbackMethod = "getOrdersByUserIdFallback")
    @CircuitBreaker(name = "orderServiceByUserId")
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        log.info("Calling OrderService...");
        ApiResponse<List<OrderDTO>> apiResponse =
                restTemplate.exchange(
                        "http://ORDERSERVICE/orders/user/{userId}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ApiResponse<List<OrderDTO>>>() {},
                        userId
                ).getBody();
        return apiResponse.getData() != null
                ? apiResponse.getData()
                : Collections.emptyList();
    }

    public List<OrderDTO> getOrdersByUserIdFallback(Long userId, Throwable ex) {
        log.error("OrderService DOWN for userId {} : {}", userId, ex.getMessage());
        return Collections.emptyList();
    }

    @CircuitBreaker(
            name = "ORDERSERVICE",
            fallbackMethod = "getAllFallback"
    )
    public ApiResponse<List<OrderWithUserIdDTO>> getAll() {
        log.info("Calling ORDERSERVICE /orders...");
        return orderService.getAll();
    }

    public ApiResponse<List<OrderWithUserIdDTO>> getAllFallback(Exception ex) {
        log.error("Order service DOWN");
        return new ApiResponse<>(
                Collections.emptyList(),
                "Order service unavailable",
                false,
                503
        );
    }

    @CircuitBreaker(
            name = "ORDERPLACE",
            fallbackMethod = "placeOrderFallback"
    )
    public ApiResponse<?> placeOrder(Order order) {
        log.info("Calling ORDERSERVICE /place orders...");
        return orderService.placeOrder(order);
    }

    public ApiResponse<?> placeOrderFallback(Order order,Exception ex) {
        log.error("Order service DOWN");
        return new ApiResponse<>(
                null,
                "Order service unavailable",
                false,
                503
        );
    }

}
