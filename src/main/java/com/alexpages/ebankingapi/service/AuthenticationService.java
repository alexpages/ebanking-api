package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.exceptions.UserAlreadyPresentException;
import com.alexpages.ebankingapi.exceptions.UserNotFoundException;
import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.client.*;
import com.alexpages.ebankingapi.utils.auth.AuthenticateRequest;
import com.alexpages.ebankingapi.utils.auth.AuthenticationResponse;
import com.alexpages.ebankingapi.utils.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<Client> clientOptional = clientService.findClientByName(request.getClientName());
        if (clientOptional.isPresent()){
            // Log
            String errorMessage = "Client with username: " + request.getClientName() + " ,could not be registered because it is already present in the DB";
            logger.error(errorMessage);
            throw new UserAlreadyPresentException(errorMessage);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // Log
        logger.debug("Password has been encoded");
        List<Account> accounts = request.getAccounts().stream()
                .map(account -> Account.builder()
                        .iban(account.getIban())
                        .currency(account.getCurrency())
                        .build())
                .collect(Collectors.toList());
        Client client = Client.builder()
                .name(request.getClientName())
                .password(encodedPassword)
                .clientRole(ClientRole.USER)
                .accounts(accounts)
                .build();
        clientService.addClient(client);
        var jwtToken = jwtService.generateToken(client);
        // Log
        logger.info("Client registered successfully: {}", client);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getClientName(),
                        request.getPassword())
        );
        var client = clientService.findClientByName(request.getClientName())
                .orElseThrow(() -> new UserNotFoundException("Client with username: " + request.getClientName() + " ,could not be authenticated because it has not been registered yet"));
        var jwtToken = jwtService.generateToken(client);
        // Log
        logger.info("Client authenticated successfully: {}", client);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
