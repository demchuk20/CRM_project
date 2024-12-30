package org.project.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project.crm.entity.Contact;
import org.project.crm.repository.ContactRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllContacts() {
        Contact contact1 = new Contact();
        contact1.setFirstName("John");
        Contact contact2 = new Contact();
        contact2.setFirstName("Jane");

        when(contactRepository.findAll()).thenReturn(Arrays.asList(contact1, contact2));

        List<Contact> contacts = contactService.findAll();
        assertEquals(2, contacts.size());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void testGetContactById() {
        Contact contact = new Contact();
        contact.setFirstName("John");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Contact foundContact = contactService.findById(1L);
        assertNotNull(foundContact);
        assertEquals("John", foundContact.getFirstName());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveContact() {
        Contact contact = new Contact();
        contact.setFirstName("John");

        when(contactRepository.save(contact)).thenReturn(contact);

        Contact savedContact = contactService.save(contact);
        assertEquals("John", savedContact.getFirstName());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void testDeleteContact() {
        doNothing().when(contactRepository).deleteById(1L);

        contactService.delete(1L);
        verify(contactRepository, times(1)).deleteById(1L);
    }
}
