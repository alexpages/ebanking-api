package com.alexpages.ebankingapi.security.auth;

import com.alexpages.ebankingapi.exceptions.UserAlreadyPresentException;
import com.alexpages.ebankingapi.exceptions.UserNotFoundException;
import com.alexpages.ebankingapi.model.client.*;
import com.alexpages.ebankingapi.security.config.JwtService;
import lombok.RequiredArgsConstructor;
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

    private final ClientRepository clientRepository;    //save client
    private final PasswordEncoder passwordEncoder;      //encode pass to save onto repo
    private final JwtService jwtService;                //for generating token
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<Client> clientOptional = clientRepository.findByEmail(request.getEmail());
        if (clientOptional.isPresent()){
            throw new UserAlreadyPresentException("User with "+ request.getEmail() + " already present in the DB");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        List<Account> accounts = request.getAccounts().stream()
                .map(account -> Account.builder()
                        .iban(account.getIban())
                        .currency(account.getCurrency())
                        .build())
                .collect(Collectors.toList());
        Client client = Client.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .accounts(accounts)
                .build();
        clientRepository.save(client);

        var jwtToken = jwtService.generateToken(client);
        return AuthenticationResponse.builder().token(jwtToken).build(); //Generate token and save it
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User by email " + request.getEmail() + " was not found"));

        var jwtToken = jwtService.generateToken(client);          //User is obtained and it is returned
        return AuthenticationResponse.builder().token(jwtToken).build(); //Auth response
    }

}
