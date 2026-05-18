package com.tasktracker.backend.controller;

import com.tasktracker.backend.dto.ApiResponse;
import com.tasktracker.backend.dto.TaskCreateRequestDTO;
import com.tasktracker.backend.dto.TaskEditRequestDTO;
import com.tasktracker.backend.dto.TaskResponseDTO;
import com.tasktracker.backend.model.entity.Users;
import com.tasktracker.backend.repository.UserRepo;
import com.tasktracker.backend.security.JwtUtil;
import com.tasktracker.backend.service.TaskService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {
    
    private final TaskService taskService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepo userRepository;
    
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    // Helper method to get current user from JWT
    private Users getCurrentUser(HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // YOUR ORIGINAL ENDPOINTS - Only change: get userId from token and pass to service
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> saveTask(
            @Valid @RequestBody TaskCreateRequestDTO task,
            HttpServletRequest request) {
        
        Users currentUser = getCurrentUser(request);  // ← ADD THIS LINE
        TaskResponseDTO savedTask = taskService.createTask(task, currentUser.getId());  // ← ADD userId
        
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.CREATED.value(), 
            "Task created",
            savedTask
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllTasks(HttpServletRequest request) {
        System.out.println("=== GET ALL TASKS DEBUG ===");
        
        Users currentUser = getCurrentUser(request);
        System.out.println("Current user ID: " + currentUser.getId());
        System.out.println("Current user email: " + currentUser.getEmail());
        
        List<TaskResponseDTO> dto = taskService.getAllTasks(currentUser.getId());
        System.out.println("Tasks found: " + dto.size());
        
        ApiResponse<List<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Tasks shown successfully",
            dto
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> findTaskById(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        Users currentUser = getCurrentUser(request);  // ← ADD THIS LINE
        TaskResponseDTO task = taskService.findById(id, currentUser.getId());  // ← ADD userId
        
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Task with "+id+" is shown successfully",
            task
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> editTask(
            @PathVariable Long id,
            @RequestBody TaskEditRequestDTO task,
            HttpServletRequest request) {
        
        Users currentUser = getCurrentUser(request);  // ← ADD THIS LINE
        TaskResponseDTO updatedTask = taskService.editTask(id, task, currentUser.getId());  // ← ADD userId
        
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Task is edited successfully",
            updatedTask
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        Users currentUser = getCurrentUser(request);  // ← ADD THIS LINE
        taskService.deleteTask(id, currentUser.getId());  // ← ADD userId
        
        ApiResponse<Void> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Task is deleted successfully",
            null
        );
        return ResponseEntity.ok(response);
    }

    // Your sorting and pagination methods (same pattern)
    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> viewTaskWithSort(
            @RequestParam String field,
            HttpServletRequest request) {
        
        Users currentUser = getCurrentUser(request);
        List<TaskResponseDTO> task = taskService.getTasksBySort(field, currentUser.getId());
        
        ApiResponse<List<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "The tasks are sorted according to "+field,
            task
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> viewTaskWithPage(
            @RequestParam int offset,
            @RequestParam int pagesize,
            HttpServletRequest request) {
        
        Users currentUser = getCurrentUser(request);
        Page<TaskResponseDTO> task = taskService.getTasksWithPage(offset, pagesize, currentUser.getId());
        
        ApiResponse<Page<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "The task with pagination shown successfully",
            task
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pagesort")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> viewTasksWithSortAndPage(
            @RequestParam int offset,
            @RequestParam int pagesize,
            @RequestParam String field,
            HttpServletRequest request) {
        
        Users currentUser = getCurrentUser(request);
        Page<TaskResponseDTO> task = taskService.getTaskWithSortAndPage(offset, pagesize, field, currentUser.getId());
        
        ApiResponse<Page<TaskResponseDTO>> response = new ApiResponse<>(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "The task with pagination and sorting shown successfully",
            task
        );
        return ResponseEntity.ok(response);
    }
}