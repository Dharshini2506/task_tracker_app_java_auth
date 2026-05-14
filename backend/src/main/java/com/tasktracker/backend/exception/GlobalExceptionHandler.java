package com.tasktracker.backend.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tasktracker.backend.dto.ErrorReponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorReponseDTO<Void>> handleTaskNotFoundException(TaskNotFoundException ex) {
        ErrorReponseDTO<Void> response = new ErrorReponseDTO<>(LocalDateTime.now(), 404, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorReponseDTO<Void>> handleInvalidDateException(InvalidDateException ex) {
        ErrorReponseDTO<Void> response = new ErrorReponseDTO<>(LocalDateTime.now(), 400, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<ErrorReponseDTO<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorReponseDTO<Void> error = new ErrorReponseDTO<>(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            errorMessage,
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorReponseDTO<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorReponseDTO<Void> error = new ErrorReponseDTO<>(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Request Body is missing",
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorReponseDTO<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorReponseDTO<Void> error = new ErrorReponseDTO<>(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Database constraint violated",
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidTaskStateException.class) 
    public ResponseEntity<ErrorReponseDTO<Void>> handleInvalidTaskStateException(InvalidTaskStateException ex) {
        ErrorReponseDTO<Void> error = new ErrorReponseDTO<>(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
