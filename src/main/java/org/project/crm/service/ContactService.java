package org.project.crm.service;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Contact;
import org.project.crm.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact findById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public void delete(Long id) {
        contactRepository.deleteById(id);
    }
}
