package com.ec.notification.service.payloads;

import com.ec.notification.service.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private UserDTO data;
    private String message;
    private boolean error;
    private int status;
}
