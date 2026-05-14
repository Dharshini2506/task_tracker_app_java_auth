package com.tasktracker.backend.model.entity;

import java.time.LocalDate;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.tasktracker.backend.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SQLDelete(sql = "UPDATE task SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "status")
    private Status status;

    @Column(nullable = false, name = "entry_date", updatable = false)
    private LocalDate entryDate;

    @Column(nullable = false, name = "due_date") 
    private LocalDate dueDate;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @PrePersist
    public void createdAt() {
        this.entryDate = LocalDate.now();
        this.status = Status.PENDING;
        this.isDeleted = false;
    }

}
