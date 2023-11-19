package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.exceptions.EbankingManagerException;
import com.alexpages.ebankingapi.domain.*;
import com.alexpages.ebankingapi.utils.AuthenticateRequest;
import com.alexpages.ebankingapi.utils.AuthenticationResponse;
import com.alexpages.ebankingapi.utils.ClientRole;
import com.alexpages.ebankingapi.utils.RegisterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class AuthenticationService {

    private ClientService clientService;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) 
    {
	    Optional<Client> clientOptional = clientService.findClientByName(request.getClientName());
	    if (clientOptional.isPresent()){
	        String errorMessage = "Client with username: " + request.getClientName() + " ,could not be registered because it is already present in the DB";
	        log.error(errorMessage);
	        throw new EbankingManagerException(errorMessage);
	    }
	    String encodedPassword = passwordEncoder.encode(request.getPassword());
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
	    log.info("Client registered successfully: {}", client);
	    return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) 
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getClientName(),
                        request.getPassword())
        );
        var client = clientService.findClientByName(request.getClientName())
                .orElseThrow(() -> new UserNotFoundException("Client with username: " + request.getClientName() + " ,could not be authenticated because it has not been registered yet"));
        var jwtToken = jwtService.generateToken(client);
        // Log
        log.info("Client authenticated successfully: {}", client);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
