package com.tasktracker.backend.repository;

import com.tasktracker.backend.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    
    // Get all tasks for a specific user
    List<Task> findByUserId(Long userId);
    
    // Get all tasks for a specific user with sorting
    List<Task> findByUserId(Long userId, Sort sort);
    
    // Get tasks for a specific user with pagination
    Page<Task> findByUserId(Long userId, Pageable pageable);
    
    // Get a single task by id AND userId (ensures user owns the task)
    Optional<Task> findByIdAndUserId(Long id, Long userId);
}