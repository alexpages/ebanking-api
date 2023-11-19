package com.alexpages.ebankingapi.web;

import com.alexpages.ebankingapi.services.AuthenticationService;
import com.alexpages.ebankingapi.utils.AuthenticateRequest;
import com.alexpages.ebankingapi.utils.AuthenticationResponse;
import com.alexpages.ebankingapi.utils.RegisterRequest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;
    //No auth required
    @Operation(
            description = "Register new user and obtain its token. No auth is required to register a new user",
            summary = "Register new user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "request", description = "It requires the Client username, its password, and the list accounts related to the Client from the client"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(
            description = "Authenticate user and retrieve its token",
            summary = "Authenticate user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "request", description = "It requires both the username and the password, in String format, from the client"
                    )
            }
    )
    @Hidden
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
