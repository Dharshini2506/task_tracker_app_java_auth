package com.tasktracker.backend.dto;

import java.time.LocalDate;

import com.tasktracker.backend.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEditRequestDTO {
    private String name;
    private String description;
    private Status status;
    private LocalDate dueDate;
}
