package com.ec.user.service.helper;

import com.ec.user.service.dtos.OrderWithUserIdDTO;
import com.ec.user.service.payloads.ApiResponse;
import com.ec.user.service.payloads.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ORDERSERVICE")
public interface OrderService {
    @GetMapping("/orders")
    ApiResponse<List<OrderWithUserIdDTO>> getAll();
    @PostMapping("/orders")
    ApiResponse<Object> placeOrder(@RequestBody Order order);
}
