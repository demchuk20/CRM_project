package org.project.crm.controller;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Client;
import org.project.crm.service.ClientService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public String getAllClients(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients/list";
    }

    @GetMapping("/{id}")
    public String getClientById(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id);
        model.addAttribute("client", client);
        return "clients/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/create";
    }

    @PostMapping
    public String createClient(@ModelAttribute Client client) {
        clientService.save(client);
        simpMessagingTemplate.convertAndSend("/topic/updates", "Client added with company name " + client.getCompanyName());
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id);
        model.addAttribute("client", client);
        return "clients/edit";
    }

    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable Long id, @ModelAttribute Client clientDetails) {
        Client client = clientService.findById(id);
        if (client != null) {
            client.setCompanyName(clientDetails.getCompanyName());
            client.setIndustry(clientDetails.getIndustry());
            client.setAddress(clientDetails.getAddress());
            clientService.save(client);
            simpMessagingTemplate.convertAndSend("/topic/updates", "Client %s updated ".formatted(client.getId()));
        }
        return "redirect:/clients";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        simpMessagingTemplate.convertAndSend("/topic/updates", "Client %s deleted ".formatted(id));
        return "redirect:/clients";
    }
}
