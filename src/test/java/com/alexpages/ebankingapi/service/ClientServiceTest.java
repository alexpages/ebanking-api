package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.ClientRole;
import com.alexpages.ebankingapi.model.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock private ClientRepository clientRepository;
    private ClientService underTest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new ClientService(clientRepository);
    }

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
        when(clientRepository.findClientByName(testClient.getName())).thenReturn(Optional.ofNullable(testClient));

        // When
        Optional<Client> foundClient = underTest.findClientByName(testClient.getName());

        // Then
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
