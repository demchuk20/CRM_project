package org.project.crm.service;

import lombok.RequiredArgsConstructor;
import org.project.crm.entity.Task;
import org.project.crm.repository.TaskRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final static String CACHE_NAME = "tasks";
    private final TaskRepository taskRepository;
    private final CacheService cacheService;

    @CacheEvict(cacheNames = CACHE_NAME)
    public Task save(Task task) {
        Task save = taskRepository.save(task);
        cacheService.evictAllCacheValues(CACHE_NAME);
        return save;
    }

    @Cacheable(CACHE_NAME)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @CacheEvict(cacheNames = CACHE_NAME)
    public void delete(Long id) {
        taskRepository.deleteById(id);
        cacheService.evictAllCacheValues(CACHE_NAME);
    }
}
