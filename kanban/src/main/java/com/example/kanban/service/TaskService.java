package com.example.kanban.service;

import com.example.kanban.model.TaskEntity;
import com.example.kanban.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskEntity> findAll() {
        // Ordenar por prioridade em ordem crescente
        return taskRepository.findAll(Sort.by(Sort.Order.asc("priority")));
    }

    public Optional<TaskEntity> findById(Long id) {
        return taskRepository.findById(id);
    }

    public TaskEntity save(TaskEntity task) {
        task.setStatus("A fazer");
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskEntity moveTask(Long id) {
        Optional<TaskEntity> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            TaskEntity task = optionalTask.get();
            String currentStatus = task.getStatus();

            switch (currentStatus) {
                case "A fazer":
                    task.setStatus("Em processo");
                    break;
                case "Em proceso":
                    task.setStatus("Concluído");
                    break;
                case "Concluído":
                    throw new IllegalStateException("A tarefa já está no último status: Concluído");
                default:
                    throw new IllegalArgumentException("Status inválido: " + currentStatus);
            }

            return taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
    }
}
