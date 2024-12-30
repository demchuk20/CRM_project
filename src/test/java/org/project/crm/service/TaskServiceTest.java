package org.project.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project.crm.entity.Task;
import org.project.crm.repository.TaskRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setDescription("Task 1");
        Task task2 = new Task();
        task2.setDescription("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.findAll();
        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setDescription("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.findById(1L);
        assertNotNull(foundTask);
        assertEquals("Task 1", foundTask.getDescription());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveTask() {
        Task task = new Task();
        task.setDescription("Task 1");

        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.save(task);
        assertEquals("Task 1", savedTask.getDescription());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.delete(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
