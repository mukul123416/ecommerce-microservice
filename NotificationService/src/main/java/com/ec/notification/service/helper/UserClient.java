package com.ec.notification.service.helper;

import com.ec.notification.service.dtos.UserDTO;
import com.ec.notification.service.payloads.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClient {

    private final UserService userService;

    public UserClient(UserService userService) {
        this.userService = userService;
    }

    @CircuitBreaker(
            name = "userServiceById",
            fallbackMethod = "userByIdFallback"
    )
    public ApiResponse<UserDTO> fetchUserBasic(Long id) {
        log.info("Calling USERSERVICE /fetch user...");
        return this.userService.fetchUserBasic(id);
    }

    public ApiResponse<UserDTO> userByIdFallback(Long id, Exception ex) {
        log.error("User service DOWN for userId {} : {}", id, ex.getMessage());
        return new ApiResponse<>(
                null,
                "User service unavailable",
                false,
                503
        );
    }

}
