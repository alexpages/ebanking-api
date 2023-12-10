package com.alexpages.ebankingapi.models;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.repository.AccountRepository;
import com.alexpages.ebankingapi.services.ClientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;

    @Mock
    private ClientService clientService;

    @Test
    void itShouldFindAccountsByClient() {
        // Given
        ClientEntity testClient = generateTestClient();

        // When
        when(clientService.addClient(testClient)).thenReturn(testClient);
        clientService.addClient(testClient);

        List<AccountEntity> accounts = underTest.findAccountsByClient(testClient);

        // Then
        assertThat(accounts).isNotNull();
    }

    private ClientEntity generateTestClient() {
        ClientEntity testClient = ClientEntity.builder()
                .clientRole(ClientRole.USER)
                .id(1)
                .name("test")
                .password("test")
                .build();
        AccountEntity testAccount = AccountEntity.builder()
                .id(1)
                .iban("iban")
                .currency("EUR")
                .client(testClient)
                .build();
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(testAccount);
        testClient.setAccounts(accounts);
        return testClient;
    }

}