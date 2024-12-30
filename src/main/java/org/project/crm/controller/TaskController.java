package org.project.crm.controller;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Task;
import org.project.crm.service.TaskService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        return "tasks/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task) {
        Task saved = taskService.save(task);
        simpMessagingTemplate.convertAndSend("/topic/updates", "Task %s was created "
                .formatted(saved.getId()));
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task taskDetails) {
        Task task = taskService.findById(id);
        if (task != null) {
            task.setDescription(taskDetails.getDescription());
            task.setStatus(taskDetails.getStatus());
            task.setDueDate(taskDetails.getDueDate());
            taskService.save(task);
            simpMessagingTemplate.convertAndSend("/topic/updates", "Task %s was updated "
                    .formatted(id));
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        simpMessagingTemplate.convertAndSend("/topic/updates", "Task %s was deleted "
                .formatted(id));
        return "redirect:/tasks";
    }
}
