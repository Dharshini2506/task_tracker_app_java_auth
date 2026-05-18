package com.tasktracker.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tasktracker.backend.model.entity.Users;

public interface UserRepo extends JpaRepository<Users, Long>{
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}