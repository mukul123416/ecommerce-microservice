package com.ec.apigateway.service.ApiGatewayService.helper;

import com.ec.apigateway.service.ApiGatewayService.payloads.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Autowired
    private ErrorResponse errorResponseUtility;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = errorResponseUtility.responseHandler(
                "Access Denied: You are not authenticated. Please add token.",
                HttpStatus.UNAUTHORIZED
        );

        byte[] bytes;
        try {
            bytes = new ObjectMapper().writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            bytes = "{\"message\":\"Authentication Error\",\"error\":true}".getBytes();
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
