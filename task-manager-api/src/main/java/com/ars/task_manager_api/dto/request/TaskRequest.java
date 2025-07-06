package com.ars.task_manager_api.dto.request;

import com.ars.task_manager_api.enumeration.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
    private UUID id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;

    @NotNull(message = "Completion status is required")
    private Boolean completed;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull
    @FutureOrPresent(message = "Due date must be Today or a future date")
    private LocalDate dueDate;
}