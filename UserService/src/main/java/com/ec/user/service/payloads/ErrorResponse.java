package com.ec.user.service.payloads;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorResponse {
    public ResponseEntity<Object> responseHandler(String message, HttpStatus status) {
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("error",true);
        map.put("status",status.value());
        return new ResponseEntity<>(map,status);
    }
}
