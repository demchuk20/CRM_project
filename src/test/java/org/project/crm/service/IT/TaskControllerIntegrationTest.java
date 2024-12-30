package org.project.crm.service.IT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.crm.entity.Client;
import org.project.crm.entity.Contact;
import org.project.crm.entity.Task;
import org.project.crm.entity.type.TaskStatus;
import org.project.crm.repository.ClientRepository;
import org.project.crm.repository.ContactRepository;
import org.project.crm.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        taskRepository.deleteAll();
        contactRepository.deleteAll();
    }

    Client setUpClient() {
        Client client = new Client();
        client.setCompanyName("Company A");
        client.setIndustry("INDUSTRY A");
        client.setAddress("Address A");
        return clientRepository.save(client);
    }

    @Test
    void testGetAllTasks() throws Exception {

        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setClient(setUpClient());
        contact = contactRepository.save(contact);

        Task task = new Task();
        task.setDescription("Task 1");
        task.setStatus(TaskStatus.OPEN);
        task.setDueDate(LocalDate.now());
        task.setContact(contact);
        taskRepository.save(task);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(model().attributeExists("tasks"));
    }

    @Test
    void testCreateTask() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setClient(setUpClient());
        contact = contactRepository.save(contact);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "Task 1")
                        .param("status", "OPEN")
                        .param("dueDate", LocalDate.now().toString())
                        .param("contact.id", contact.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        Task task = taskRepository.findAll().get(0);
        assertEquals("Task 1", task.getDescription());
    }

    @Test
    void testUpdateTask() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setClient(setUpClient());
        contact = contactRepository.save(contact);

        Task task = new Task();
        task.setDescription("Task 1");
        task.setStatus(TaskStatus.OPEN);
        task.setDueDate(LocalDate.now());
        task.setContact(contact);
        task = taskRepository.save(task);

        mockMvc.perform(post("/tasks/update/" + task.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "Task 2")
                        .param("status", "IN_PROGRESS")
                        .param("dueDate", LocalDate.now().plusDays(1).toString())
                        .param("contact.id", contact.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        Task updatedTask = taskRepository.findById(task.getId()).orElse(null);
        assertNotNull(updatedTask);
        assertEquals("Task 2", updatedTask.getDescription());
    }

    @Test
    void testDeleteTask() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setClient(setUpClient());
        contact = contactRepository.save(contact);

        Task task = new Task();
        task.setDescription("Task 1");
        task.setStatus(TaskStatus.OPEN);
        task.setDueDate(LocalDate.now());
        task.setContact(contact);
        task = taskRepository.save(task);

        mockMvc.perform(get("/tasks/delete/" + task.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        assertFalse(taskRepository.findById(task.getId()).isPresent());
    }
}