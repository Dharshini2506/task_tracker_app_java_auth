// package com.tasktracker.backend.service;

// import java.time.LocalDate;
// import java.util.List;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Sort;
// import org.springframework.stereotype.Service;

// import com.tasktracker.backend.dto.TaskCreateRequestDTO;
// import com.tasktracker.backend.dto.TaskEditRequestDTO;
// import com.tasktracker.backend.dto.TaskResponseDTO;
// import com.tasktracker.backend.exception.InvalidDateException;
// import com.tasktracker.backend.exception.InvalidTaskStateException;
// import com.tasktracker.backend.exception.TaskNotFoundException;
// import com.tasktracker.backend.model.entity.Task;
// import com.tasktracker.backend.model.enums.Status;
// import com.tasktracker.backend.repository.TaskRepo;

// @Service
// public class TaskService {
    
//     private final TaskRepo taskRepo;

//     public TaskService(TaskRepo taskRepo) {
//         this.taskRepo = taskRepo;
//     }

//     public TaskResponseDTO createTask(TaskCreateRequestDTO dto) {

//         Task task = new Task();
//         task.setName(dto.getName());
//         task.setDueDate(dto.getDueDate());

//         LocalDate today = LocalDate.now();

//         if(task.getDueDate() == null || task.getDueDate().isBefore(today)) {
//             throw new InvalidDateException("Due date must be today or future date");
//         }

//         Task savedTask = taskRepo.save(task);

//         return new TaskResponseDTO(
//             task.getId(),
//             savedTask.getName(),
//             savedTask.getStatus(),
//             savedTask.getEntryDate(),
//             savedTask.getDueDate()
//         );
//     }

//     public List<TaskResponseDTO> getAllTasks() {
//         return taskRepo.findByIsDeletedFalse()
//             .stream()
//             .map(task -> new TaskResponseDTO(
//                 task.getId(),
//                 task.getName(),
//                 task.getStatus(),
//                 task.getEntryDate(),
//                 task.getDueDate()
//             ))
//             .toList();
//     }

//     public TaskResponseDTO findById(Long id) {
//         Task task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id "+id+" is not found"));

//         return new TaskResponseDTO(
//             task.getId(),
//             task.getName(),
//             task.getStatus(),
//             task.getEntryDate(),
//             task.getDueDate()
//         );
//     }

//     public void deleteTask(Long id) {
//         Task task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id "+id+" is not found"));

//         taskRepo.delete(task);
//     }

//     public TaskResponseDTO editTask(Long id, TaskEditRequestDTO updatedTask) {
//         Task task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id "+id+" is not found"));

//         if(task.getStatus() == Status.COMPLETED) {
//             throw new InvalidTaskStateException("Completed task cannot be edited");
//         }

//         LocalDate today = LocalDate.now();

//         if(updatedTask.getName()!=null) {
//             task.setName(updatedTask.getName());
//         }

//         if(updatedTask.getStatus()!=null) {
//             if(task.getStatus() == Status.COMPLETED && updatedTask.getStatus() == Status.PENDING) {
//                 throw new InvalidTaskStateException("Completed task cannot be changed to pending");
//             }
//             task.setStatus(updatedTask.getStatus());
//         }

//         if(updatedTask.getDueDate()!=null) {
//             if(updatedTask.getDueDate().isBefore(today)) {
//                 throw new InvalidDateException("Due date must be today or future date");
//             }
//             task.setDueDate(updatedTask.getDueDate());
//         }

//         Task savedTask = taskRepo.save(task);

//         return new TaskResponseDTO(
//             task.getId(),
//             savedTask.getName(),
//             savedTask.getStatus(),
//             savedTask.getEntryDate(),
//             savedTask.getDueDate()
//         );
//     }

//     public List<TaskResponseDTO> getTasksBySort(String field) {
//         return taskRepo.findAll(Sort.by(field))
//         .stream()
//         .map(task -> new TaskResponseDTO(
//             task.getId(),
//             task.getName(),
//             task.getStatus(),
//             task.getEntryDate(),
//             task.getDueDate()
//         ))
//         .toList();
//     }

//     public Page<TaskResponseDTO> getTasksWithPage(int offset, int pagesize) {
//         return taskRepo.findAll(PageRequest.of(offset, pagesize))
//         .map(task -> new TaskResponseDTO(
//             task.getId(),
//             task.getName(),
//             task.getStatus(),
//             task.getEntryDate(),
//             task.getDueDate()
//         ));
//     }

//     public Page<TaskResponseDTO> getTaskWithSortAndPage(int offset, int pagesize, String field) {
//         return taskRepo.findAll(PageRequest.of(offset, pagesize, Sort.by(field)))
//         .map(task -> new TaskResponseDTO(
//             task.getId(),
//             task.getName(),
//             task.getStatus(),
//             task.getEntryDate(),
//             task.getDueDate()
//         ));
//     }
// }


package com.tasktracker.backend.service;

import com.tasktracker.backend.dto.TaskCreateRequestDTO;
import com.tasktracker.backend.dto.TaskEditRequestDTO;
import com.tasktracker.backend.dto.TaskResponseDTO;
import com.tasktracker.backend.exception.TaskNotFoundException;
import com.tasktracker.backend.model.entity.Task;
import com.tasktracker.backend.model.entity.Users;
import com.tasktracker.backend.repository.TaskRepo;
import com.tasktracker.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepo taskRepository;
    
    @Autowired
    private UserRepo userRepository;
    
    // CREATE Task (with userId)
    public TaskResponseDTO createTask(TaskCreateRequestDTO taskDTO, Long userId) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription() != null ? taskDTO.getDescription() : ""); // ← FIX
        task.setDueDate(taskDTO.getDueDate());
        task.setUser(user);  // ASSIGN TO USER
        
        Task saved = taskRepository.save(task);
        return convertToDTO(saved);
    }
    
    // GET All Tasks (only user's tasks)
    public List<TaskResponseDTO> getAllTasks(Long userId) {
        System.out.println("=== TaskService.getAllTasks ===");
        System.out.println("Looking for tasks with userId: " + userId);
        
        List<Task> tasks = taskRepository.findByUserId(userId);
        System.out.println("Repository returned: " + tasks.size() + " tasks");
        
        for (Task t : tasks) {
            System.out.println("Task ID: " + t.getId() + ", Name: " + t.getName() + ", User ID: " + t.getUser().getId());
        }
        
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    // GET Task by ID (only if belongs to user)
    public TaskResponseDTO findById(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return convertToDTO(task);
    }
    
    // EDIT Task (only if belongs to user)
    public TaskResponseDTO editTask(Long id, TaskEditRequestDTO taskDTO, Long userId) {
        Task existingTask = taskRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        if (taskDTO.getName() != null) {
            existingTask.setName(taskDTO.getName());
        }
        if (taskDTO.getStatus() != null) {
            existingTask.setStatus(taskDTO.getStatus());
        }
        if (taskDTO.getDescription() != null) {
            existingTask.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getDueDate() != null) {
            existingTask.setDueDate(taskDTO.getDueDate());
        }
        
        Task updated = taskRepository.save(existingTask);
        return convertToDTO(updated);
    }
    
    // DELETE Task (only if belongs to user)
    public void deleteTask(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
    }
    
    // SORT (only user's tasks)
    public List<TaskResponseDTO> getTasksBySort(String field, Long userId) {
        Sort sort = Sort.by(Sort.Direction.ASC, field);
        List<Task> tasks = taskRepository.findByUserId(userId, sort);  // ← FIXED: (userId, sort) not (Long, sort)
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // PAGINATION (only user's tasks)
    public Page<TaskResponseDTO> getTasksWithPage(int offset, int pageSize, Long userId) {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Task> taskPage = taskRepository.findByUserId(userId, pageable);  // ← FIXED
        return taskPage.map(this::convertToDTO);
    }

    // SORT + PAGINATION (only user's tasks)
    public Page<TaskResponseDTO> getTaskWithSortAndPage(int offset, int pageSize, String field, Long userId) {
        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(field).ascending());
        Page<Task> taskPage = taskRepository.findByUserId(userId, pageable);  // ← FIXED
        return taskPage.map(this::convertToDTO);
    }

    // Helper method to convert Entity to DTO
    private TaskResponseDTO convertToDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setStatus(task.getStatus());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setEntryDate(task.getEntryDate());
        return dto;
    }
}