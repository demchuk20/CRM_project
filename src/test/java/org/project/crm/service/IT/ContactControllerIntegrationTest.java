package org.project.crm.service.IT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.crm.entity.Client;
import org.project.crm.entity.Contact;
import org.project.crm.repository.ClientRepository;
import org.project.crm.repository.ContactRepository;
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
class ContactControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        contactRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void testGetAllContacts() throws Exception {
        Client client = new Client();
        client.setCompanyName("Company A");
        client = clientRepository.save(client);

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setClient(client);
        contactRepository.save(contact);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts/list"))
                .andExpect(model().attributeExists("contacts"));
    }

    @Test
    void testCreateContact() throws Exception {
        Client client = new Client();
        client.setCompanyName("Company A");
        client = clientRepository.save(client);

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com")
                        .param("phone", "1234567890")
                        .param("client.id", client.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));

        Contact contact = contactRepository.findAll().get(0);
        assertEquals("John", contact.getFirstName());
    }

    @Test
    void testUpdateContact() throws Exception {
        Client client = new Client();
        client.setCompanyName("Company A");
        client = clientRepository.save(client);

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setClient(client);
        contact = contactRepository.save(contact);

        mockMvc.perform(post("/contacts/update/" + contact.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Jane")
                        .param("lastName", "Doe")
                        .param("email", "jane.doe@example.com")
                        .param("phone", "0987654321")
                        .param("client.id", client.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));

        Contact updatedContact = contactRepository.findById(contact.getId()).orElse(null);
        assertNotNull(updatedContact);
        assertEquals("Jane", updatedContact.getFirstName());
    }

    @Test
    void testDeleteContact() throws Exception {
        Client client = new Client();
        client.setCompanyName("Company A");
        client = clientRepository.save(client);
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setClient(client);
        contact = contactRepository.save(contact);

        mockMvc.perform(get("/contacts/delete/" + contact.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));

        assertFalse(contactRepository.findById(contact.getId()).isPresent());
    }
}