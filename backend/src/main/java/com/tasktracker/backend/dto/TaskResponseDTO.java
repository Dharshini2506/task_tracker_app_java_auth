package com.tasktracker.backend.dto;

import java.time.LocalDate;

import com.tasktracker.backend.model.enums.Status;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskResponseDTO {
    private Long id;
    @NotNull(message = "Task name is a must")
    private String name;
    private Status status;
    private LocalDate entryDate;
    @NotNull(message = "DueDate is a must")
    private LocalDate dueDate;
}
