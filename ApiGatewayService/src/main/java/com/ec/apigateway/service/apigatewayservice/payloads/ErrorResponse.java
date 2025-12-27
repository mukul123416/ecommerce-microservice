package com.ec.apigateway.service.apigatewayservice.payloads;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorResponse {
    public Map<String, Object> responseHandler(String message, HttpStatus status) {
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("error",true);
        map.put("status",status.value());
        return map;
    }
}
