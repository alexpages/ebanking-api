package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.exceptions.UserNotFoundException;
import com.alexpages.ebankingapi.models.client.Client;
import com.alexpages.ebankingapi.models.client.ClientRepository;
import com.alexpages.ebankingapi.models.client.ClientRole;
import com.alexpages.ebankingapi.models.account.Account;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock private ClientRepository clientRepository;
    @InjectMocks
    private ClientService underTest;

    @Test
    void canAddClient() {
        // Given
        Client testClient = generateTestClient();

        // When
        underTest.addClient(testClient);

        // Then
        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientArgumentCaptor.capture());

        Client capturedClient = clientArgumentCaptor.getValue();
        assertThat(capturedClient).isEqualTo(testClient);
    }

    @Test
    void itShouldFindClientByName() {
        // Given
        Client testClient = generateTestClient();
        Optional<Client> expectedClient = Optional.of(testClient);

        // When
        when(underTest.findClientByName(testClient.getName())).thenReturn(Optional.ofNullable(testClient));
        Optional<Client> foundClient = underTest.findClientByName(testClient.getName());

        // Then
        verify(clientRepository).findClientByName(testClient.getName());
        assertThat(foundClient).isEqualTo(expectedClient);
    }

    @Test
    void itShouldFindClientByAccount() {
        // Given
        Client testClient = generateTestClient();
        String account = testClient.getAccounts().get(0).getIban();

        // When
        when(clientRepository.findClientByAccount(account)).thenReturn(testClient);
        Client foundClient = underTest.findClientByAccount(account);

        // Then
        verify(clientRepository).findClientByAccount(account);
        assertThat(foundClient).isEqualTo(testClient);
    }

    @Test
    void itShouldThrowRuntimeExceptionWhenClientNotFound() {
        // Given
        String nonExistentIban = "nonExistentAccount";

        // When
        when(clientRepository.findClientByAccount(nonExistentIban)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            underTest.findClientByAccount(nonExistentIban);
        });

        // Then
        String expectedMessage = String.format("Client with iban: "+ nonExistentIban +", could not be found");
        assertEquals(expectedMessage, exception.getMessage());
        verify(clientRepository).findClientByAccount(nonExistentIban);
    }

    private Client generateTestClient() {
        Client testClient = Client.builder()
                .clientRole(ClientRole.USER)
                .id(1)
                .name("test")
                .password("test")
                .build();
        Account testAccount = Account.builder()
                .id(1)
                .iban("iban")
                .currency("EUR")
                .client(testClient)
                .build();
        List<Account> accounts = new ArrayList<>();
        accounts.add(testAccount);
        testClient.setAccounts(accounts);
        return testClient;
    }
}
