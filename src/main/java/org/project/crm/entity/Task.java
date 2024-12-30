package org.project.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.project.crm.entity.type.TaskStatus;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
}
