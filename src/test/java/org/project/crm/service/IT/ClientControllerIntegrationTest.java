package org.project.crm.service.IT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.crm.entity.Client;
import org.project.crm.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        clientRepository.deleteAll();
    }

    @Test
    void testGetAllClients() throws Exception {
        Client client = new Client();
        client.setCompanyName("Company A");
        clientRepository.save(client);

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/list"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void testCreateClient() throws Exception {
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("companyName", "Company A")
                        .param("industry", "Industry A")
                        .param("address", "Address A"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        Client client = clientRepository.findAll().get(0);
        assertEquals("Company A", client.getCompanyName());
    }

    @Test
    void testUpdateClient() throws Exception {
        Client client = new Client();
        client.setCompanyName("Company A");
        client = clientRepository.save(client);

        mockMvc.perform(post("/clients/update/" + client.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("companyName", "Company B")
                        .param("industry", "Industry B")
                        .param("address", "Address B"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        Client updatedClient = clientRepository.findById(client.getId()).orElse(null);
        assertNotNull(updatedClient);
        assertEquals("Company B", updatedClient.getCompanyName());
    }

    @Test
    void testDeleteClient() throws Exception {
        Client client = new Client();
        client.setCompanyName("Company A");
        client = clientRepository.save(client);

        mockMvc.perform(get("/clients/delete/" + client.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        assertFalse(clientRepository.findById(client.getId()).isPresent());
    }
}