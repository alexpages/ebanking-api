package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.ClientRole;
import com.alexpages.ebankingapi.model.account.Account;
import org.junit.jupiter.api.BeforeEach;
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
        Optional<Client> expectedClient = Optional.of(testClient);

        // When
        when(clientRepository.findClientByAccount(testClient.getAccounts().get(0).getIban()))
                .thenReturn(testClient);
        Optional<Client> foundClient = Optional.ofNullable(underTest.findClientByAccount(testClient.getAccounts().get(0).getIban()));

        // Then
        verify(clientRepository).findClientByAccount(testClient.getAccounts().get(0).getIban());
        assertThat(foundClient).isEqualTo(expectedClient);
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
