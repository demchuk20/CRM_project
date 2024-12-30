package org.project.crm.repository;

import org.project.crm.entity.Task;
import org.project.crm.repository.jpa.TaskRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
}
