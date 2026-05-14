package com.tasktracker.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tasktracker.backend.dto.ApiResponse;
import com.tasktracker.backend.dto.TaskCreateRequestDTO;
import com.tasktracker.backend.dto.TaskEditRequestDTO;
import com.tasktracker.backend.dto.TaskResponseDTO;
import com.tasktracker.backend.service.TaskService;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> saveTask(@RequestBody TaskCreateRequestDTO task) {
        TaskResponseDTO savedTask = taskService.createTask(task);
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.CREATED.value(), 
            "Task created",
            savedTask
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllTasks() {
        List<TaskResponseDTO> dto = taskService.getAllTasks();
        ApiResponse<List<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Tasks shown successfully",
            dto
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> findTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.findById(id);
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Task with "+id+"is shown sucessfully",
            task
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> editTask(@PathVariable Long id, @RequestBody TaskEditRequestDTO task) {
        TaskResponseDTO updatedTask = taskService.editTask(id, task);
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Task is edited successfully",
            updatedTask
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        ApiResponse<Void> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Task is deleted successfully",
            null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> viewTaskWithSort(@RequestParam String field) {
        List<TaskResponseDTO> task = taskService.getTasksBySort(field);
        ApiResponse<List<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "The tasks are sorted according to "+field,
            task
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> viewTaskWithPage(@RequestParam int offset,@RequestParam int pagesize) {
        Page<TaskResponseDTO> task = taskService.getTasksWithPage(offset, pagesize);
        ApiResponse<Page<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "The task with pagination shown successfully",
            task
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pagesort")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> viewTasksWithSortAndPage(@RequestParam int offset, @RequestParam int pagesize, @RequestParam String field) {
        Page<TaskResponseDTO> task = taskService.getTaskWithSortAndPage(offset, pagesize, field);
        ApiResponse<Page<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "The task with pagination and sorting shown successfully",
            task
        );
        return ResponseEntity.ok(response);
    }
}