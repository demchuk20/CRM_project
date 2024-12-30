package org.project.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project.crm.entity.Client;
import org.project.crm.repository.ClientRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClients() {
        Client client1 = new Client();
        client1.setCompanyName("Company A");
        Client client2 = new Client();
        client2.setCompanyName("Company B");

        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));

        List<Client> clients = clientService.findAll();
        assertEquals(2, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        Client client = new Client();
        client.setCompanyName("Company A");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client foundClient = clientService.findById(1L);
        assertEquals("Company A", foundClient.getCompanyName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveClient() {
        Client client = new Client();
        client.setCompanyName("Company A");

        when(clientRepository.save(client)).thenReturn(client);

        Client savedClient = clientService.save(client);
        assertEquals("Company A", savedClient.getCompanyName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testDeleteClient() {
        doNothing().when(clientRepository).deleteById(1L);

        clientService.delete(1L);
        verify(clientRepository, times(1)).deleteById(1L);
    }
}
