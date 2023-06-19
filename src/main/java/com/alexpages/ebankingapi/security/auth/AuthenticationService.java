package com.alexpages.ebankingapi.security.auth;

import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.Role;
import com.alexpages.ebankingapi.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository repository;      //save client
    private final PasswordEncoder passwordEncoder;  //encode pass to save onto repo
    private final JwtService jwtService;            //for generating token
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var client = Client.builder()
                .accounts(request.getAccounts())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(client);

        var jwtToken = jwtService.generateToken(client);
        return AuthenticationResponse.builder().token(jwtToken).build(); //Generate token and save it
    }

    public Object authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getClientId(),
                        request.getPassword())
        );

        return null;
    }
}
