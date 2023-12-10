package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.exceptions.EbankingManagerException;
import com.alexpages.ebankingapi.others.AuthenticateRequest;
import com.alexpages.ebankingapi.others.AuthenticationResponse;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.others.RegisterRequest;

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

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	private ClientService clientService;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) 
    {
	    Optional<ClientEntity> clientOptional = clientService.findClientByName(request.getClientName());
	    if (clientOptional.isPresent()){
	        String errorMessage = "Client with username: " + request.getClientName() + " ,could not be registered because it is already present in the DB";
	        logger.error(errorMessage);
	        throw new EbankingManagerException(errorMessage);
	    }
	    String encodedPassword = passwordEncoder.encode(request.getPassword());
	    List<AccountEntity> accounts = request.getAccounts().stream()
	            .map(account -> AccountEntity.builder()
	                    .iban(account.getIban())
	                    .currency(account.getCurrency())
	                    .build())
	            .collect(Collectors.toList());
	    ClientEntity client = ClientEntity.builder()
	            .name(request.getClientName())
	            .password(encodedPassword)
	            .clientRole(ClientRole.USER)
	            .accounts(accounts)
	            .build();
	    clientService.addClient(client);
	    String jwtToken = jwtService.generateToken(client);
	    // Log
	    logger.info("Client registered successfully: {}", client);
	    return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) 
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getClientName(),
                        request.getPassword())
        );
        ClientEntity client = clientService.findClientByName(request.getClientName())
                .orElseThrow(() -> new EbankingManagerException("Client with username: " + request.getClientName() + " ,could not be authenticated because it has not been registered yet"));
        String jwtToken = jwtService.generateToken(client);
        // Log
        logger.info("Client authenticated successfully: {}", client);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
