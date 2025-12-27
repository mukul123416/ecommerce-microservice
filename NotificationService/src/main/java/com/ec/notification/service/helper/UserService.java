package com.ec.notification.service.helper;

import com.ec.notification.service.dtos.UserDTO;
import com.ec.notification.service.payloads.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERSERVICE")
public interface UserService {
    @GetMapping("/users/basic/{id}")
    ApiResponse<UserDTO> fetchUserBasic(@PathVariable Long id);
}
