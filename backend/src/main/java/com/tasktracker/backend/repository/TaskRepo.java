package com.tasktracker.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasktracker.backend.model.entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long>{
    List<Task> findByIsDeletedFalse();
}
