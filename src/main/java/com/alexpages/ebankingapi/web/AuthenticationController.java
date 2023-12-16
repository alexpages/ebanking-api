package com.alexpages.ebankingapi.web;

import com.alexpages.ebankingapi.others.AuthenticateRequest;
import com.alexpages.ebankingapi.others.AuthenticationResponse;
import com.alexpages.ebankingapi.others.RegisterRequest;
import com.alexpages.ebankingapi.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
		
    private AuthenticationService authenticationService;
    
    @Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		super();
		this.authenticationService =authenticationService;
	}
  
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Hidden
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
