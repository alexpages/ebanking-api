package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.exceptions.UserAlreadyPresentException;
import com.alexpages.ebankingapi.exceptions.UserNotFoundException;
import com.alexpages.ebankingapi.utils.AuthenticateRequest;
import com.alexpages.ebankingapi.utils.AuthenticationResponse;
import com.alexpages.ebankingapi.utils.RegisterRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private ClientService clientService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService underTest;

    @Test
    void itShouldRegisterClient() {
        // Given
        Account account = Mockito.mock(Account.class);
        RegisterRequest request = RegisterRequest.builder()
                .clientName("test")
                .password("password")
                .accounts(Collections.singletonList(account))
                .build();

        // When
        when(clientService.findClientByName(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(Client.class))).thenReturn("jwtToken");
        AuthenticationResponse response = underTest.register(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getToken());
    }
    @Test
    void itShouldNotRegisterClient() {
        // Given
        String clientName = "existingClient";
        Account account = Mockito.mock(Account.class);
        RegisterRequest request = RegisterRequest.builder()
                .clientName(clientName)
                .password("password")
                .accounts(Collections.singletonList(account))
                .build();

        // When
        Client existingClient = Client.builder().name(clientName).build();
        when(clientService.findClientByName(clientName)).thenReturn(Optional.of(existingClient));

        // Then
        UserAlreadyPresentException exception = Assertions.assertThrows(UserAlreadyPresentException.class, () -> {
            underTest.register(request);
        });
        assertEquals("Client with username: " + request.getClientName() + " ,could not be registered because it is already present in the DB", exception.getMessage());

    }
    @Test
    void itShouldAuthenticateClient() {
        // Given
        AuthenticateRequest request = AuthenticateRequest.builder()
                .clientName("test")
                .password("password")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getClientName(),
                request.getPassword());

        // When
        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        when(clientService.findClientByName(request.getClientName())).thenReturn(Optional.of(new Client()));
        when(jwtService.generateToken(any(Client.class))).thenReturn("jwtToken");

        // Then
        AuthenticationResponse response = underTest.authenticate(request);
        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @Test
    void itShouldNotAuthenticateClient() {
        // Given
        AuthenticateRequest request = AuthenticateRequest.builder()
                .clientName("test")
                .password("password")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getClientName(),
                request.getPassword());

        // When
        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);

        // Then
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            underTest.authenticate(request);
        });
        assertEquals("Client with username: " + request.getClientName() + " ,could not be authenticated because it has not been registered yet", exception.getMessage());
    }
}