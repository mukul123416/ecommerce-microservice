package com.ec.apigateway.service.ApiGatewayService.controllers;
import com.ec.apigateway.service.ApiGatewayService.helper.AuthResponse;
import com.ec.apigateway.service.ApiGatewayService.payloads.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ErrorResponse errorResponse;

    @Operation(
            summary = "Generate token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",  content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"userId\": \"gopiha9199@mekuron.com\",\n" +
                                    "  \"accessToken\": \"eyJraWQi...\",\n" +
                                    "  \"refreshToken\": \"c9T5K32...\",\n" +
                                    "  \"expireAt\": 1766230172,\n" +
                                    "  \"authorities\": [\n" +
                                    "    \"OIDC_USER\",\n" +
                                    "    \"ROLE_ADMIN\",\n" +
                                    "    \"ROLE_EVERYONE\",\n" +
                                    "    \"SCOPE_email\"\n" +
                                    "  ]\n" +
                                    "}")
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request",content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\n" +
                                    "  \"message\": \"{$error_message}\",\n" +
                                    "  \"error\": true,\n" +
                                    "  \"status\": 400\n" +
                                    "}")
                    ))
            }
    )
    @GetMapping("/login")
    public ResponseEntity<?> login(
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
                    .collect(Collectors.toList());

            authResponse.setAuthorities(authorities);

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, Object> map = errorResponse.responseHandler(ex.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }

    }
}
