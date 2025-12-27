package com.ec.apigateway.service.apigatewayservice.controllers;
import com.ec.apigateway.service.apigatewayservice.helper.AuthResponse;
import com.ec.apigateway.service.apigatewayservice.payloads.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ErrorResponse errorResponse;

    public AuthController(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    @Operation(
            summary = "Generate token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "userId": "gopiha9199@mekuron.com",
                                      "accessToken": "eyJraWQi...",
                                      "refreshToken": "c9T5K32...",
                                      "expireAt": 1766230172,
                                      "authorities": [
                                        "OIDC_USER",
                                        "ROLE_ADMIN",
                                        "ROLE_EVERYONE",
                                        "SCOPE_email"
                                      ]
                                    }
                                    """)
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "message": "{$error_message}",
                                      "error": true,
                                      "status": 400
                                    }
                                    """)
                    ))
            }
    )
    @GetMapping("/login")
    public ResponseEntity<Object> login(
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OidcUser user,
            Model model
    ) {

        try {

            AuthResponse authResponse = new AuthResponse();

            authResponse.setUserId(user.getEmail());

            authResponse.setAccessToken(client.getAccessToken().getTokenValue());

            authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());

            authResponse.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());

            List<String> authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            authResponse.setAuthorities(authorities);

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, Object> map = this.errorResponse.responseHandler(ex.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }

    }
}
