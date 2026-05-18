package com.tasktracker.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateRequestDTO {
    @NotNull(message = "Task name is a must")
    private String name;
    @NotNull(message = "DueDate is a must")
    private LocalDate dueDate;
    private String description;
}
