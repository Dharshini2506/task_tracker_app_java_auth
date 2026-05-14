package com.tasktracker.backend.exception;

public class InvalidTaskStateException extends RuntimeException{
    public InvalidTaskStateException(String message) {
        super(message);
    }
}
