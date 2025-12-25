package com.ec.notification.service.helper;

import com.ec.notification.service.payloads.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClient {
    @Autowired
    private UserService userService;

    @CircuitBreaker(
            name = "userServiceById",
            fallbackMethod = "userByIdFallback"
    )
    public ApiResponse<?> fetchUserBasic(Long id) {
        log.info("Calling USERSERVICE /fetch user...");
        return userService.fetchUserBasic(id);
    }

    public ApiResponse<?> userByIdFallback(Long id,Exception ex) {
        log.error("User service DOWN");
        return new ApiResponse<>(
                null,
                "User service unavailable",
                false,
                503
        );
    }

}
