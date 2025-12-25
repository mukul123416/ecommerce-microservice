package com.ec.apigateway.service.ApiGatewayService.helper;
import lombok.*;

import java.util.Collection;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private long expireAt;
    private Collection<String> authorities;
}
