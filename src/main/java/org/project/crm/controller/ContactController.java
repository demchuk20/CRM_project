package org.project.crm.controller;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Contact;
import org.project.crm.service.ContactService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public String getAllContacts(Model model) {
        List<Contact> contacts = contactService.findAll();
        model.addAttribute("contacts", contacts);
        return "contacts/list";
    }

    @GetMapping("/{id}")
    public String getContactById(@PathVariable Long id, Model model) {
        Contact contact = contactService.findById(id);
        model.addAttribute("contact", contact);
        return "contacts/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contacts/create";
    }

    @PostMapping
    public String createContact(@ModelAttribute Contact contact) {
        Contact saved = contactService.save(contact);
        simpMessagingTemplate.convertAndSend("/topic/updates", "Contact %s was created "
                .formatted(saved.getId()));

        return "redirect:/contacts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Contact contact = contactService.findById(id);
        model.addAttribute("contact", contact);
        return "contacts/edit";
    }

    @PostMapping("/update/{id}")
    public String updateContact(@PathVariable Long id, @ModelAttribute Contact contactDetails) {
        Contact contact = contactService.findById(id);
        if (contact != null) {
            contact.setFirstName(contactDetails.getFirstName());
            contact.setLastName(contactDetails.getLastName());
            contact.setEmail(contactDetails.getEmail());
            contact.setPhone(contactDetails.getPhone());
            contactService.save(contact);
            simpMessagingTemplate.convertAndSend("/topic/updates", "Contact %s was updated "
                    .formatted(contact.getId()));
        }
        return "redirect:/contacts";
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.delete(id);
        simpMessagingTemplate.convertAndSend("/topic/updates", "Contact %s was deleted "
                .formatted(id));
        return "redirect:/contacts";
    }
}
