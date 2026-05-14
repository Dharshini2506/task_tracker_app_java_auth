package com.tasktracker.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tasktracker.backend.dto.TaskCreateRequestDTO;
import com.tasktracker.backend.dto.TaskEditRequestDTO;
import com.tasktracker.backend.dto.TaskResponseDTO;
import com.tasktracker.backend.exception.InvalidDateException;
import com.tasktracker.backend.exception.InvalidTaskStateException;
import com.tasktracker.backend.exception.TaskNotFoundException;
import com.tasktracker.backend.model.entity.Task;
import com.tasktracker.backend.model.enums.Status;
import com.tasktracker.backend.repository.TaskRepo;

@Service
public class TaskService {
    
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public TaskResponseDTO createTask(TaskCreateRequestDTO dto) {

        Task task = new Task();
        task.setName(dto.getName());
        task.setDueDate(dto.getDueDate());

        LocalDate today = LocalDate.now();

        if(task.getDueDate() == null || task.getDueDate().isBefore(today)) {
            throw new InvalidDateException("Due date must be today or future date");
        }

        Task savedTask = taskRepo.save(task);

        return new TaskResponseDTO(
            task.getId(),
            savedTask.getName(),
            savedTask.getStatus(),
            savedTask.getEntryDate(),
            savedTask.getDueDate()
        );
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepo.findByIsDeletedFalse()
            .stream()
            .map(task -> new TaskResponseDTO(
                task.getId(),
                task.getName(),
                task.getStatus(),
                task.getEntryDate(),
                task.getDueDate()
            ))
            .toList();
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id "+id+" is not found"));

        return new TaskResponseDTO(
            task.getId(),
            task.getName(),
            task.getStatus(),
            task.getEntryDate(),
            task.getDueDate()
        );
    }

    public void deleteTask(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id "+id+" is not found"));

        taskRepo.delete(task);
    }

    public TaskResponseDTO editTask(Long id, TaskEditRequestDTO updatedTask) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id "+id+" is not found"));

        if(task.getStatus() == Status.COMPLETED) {
            throw new InvalidTaskStateException("Completed task cannot be edited");
        }

        LocalDate today = LocalDate.now();

        if(updatedTask.getName()!=null) {
            task.setName(updatedTask.getName());
        }

        if(updatedTask.getStatus()!=null) {
            if(task.getStatus() == Status.COMPLETED && updatedTask.getStatus() == Status.PENDING) {
                throw new InvalidTaskStateException("Completed task cannot be changed to pending");
            }
            task.setStatus(updatedTask.getStatus());
        }

        if(updatedTask.getDueDate()!=null) {
            if(updatedTask.getDueDate().isBefore(today)) {
                throw new InvalidDateException("Due date must be today or future date");
            }
            task.setDueDate(updatedTask.getDueDate());
        }

        Task savedTask = taskRepo.save(task);

        return new TaskResponseDTO(
            task.getId(),
            savedTask.getName(),
            savedTask.getStatus(),
            savedTask.getEntryDate(),
            savedTask.getDueDate()
        );
    }

    public List<TaskResponseDTO> getTasksBySort(String field) {
        return taskRepo.findAll(Sort.by(field))
        .stream()
        .map(task -> new TaskResponseDTO(
            task.getId(),
            task.getName(),
            task.getStatus(),
            task.getEntryDate(),
            task.getDueDate()
        ))
        .toList();
    }

    public Page<TaskResponseDTO> getTasksWithPage(int offset, int pagesize) {
        return taskRepo.findAll(PageRequest.of(offset, pagesize))
        .map(task -> new TaskResponseDTO(
            task.getId(),
            task.getName(),
            task.getStatus(),
            task.getEntryDate(),
            task.getDueDate()
        ));
    }

    public Page<TaskResponseDTO> getTaskWithSortAndPage(int offset, int pagesize, String field) {
        return taskRepo.findAll(PageRequest.of(offset, pagesize, Sort.by(field)))
        .map(task -> new TaskResponseDTO(
            task.getId(),
            task.getName(),
            task.getStatus(),
            task.getEntryDate(),
            task.getDueDate()
        ));
    }
}
