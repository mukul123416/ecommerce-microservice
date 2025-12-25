package com.ec.user.service.payloads;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SuccessResponse {
    public ResponseEntity<Object> responseHandler(String message,HttpStatus status, Object data){
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("error",false);
        map.put("status",status.value());
        map.put("data",data);
        return new ResponseEntity<>(map,status);
    }
}

