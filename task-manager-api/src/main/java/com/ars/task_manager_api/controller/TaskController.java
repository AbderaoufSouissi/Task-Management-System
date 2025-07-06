package com.ars.task_manager_api.controller;

import com.ars.task_manager_api.dto.request.TaskRequest;
import com.ars.task_manager_api.dto.response.Response;
import com.ars.task_manager_api.entity.Task;
import com.ars.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Response<Task>> createTask(@RequestBody @Valid TaskRequest taskRequest) {
        return new ResponseEntity<>(taskService.createTask(taskRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Response<Task>> updateTask(@RequestBody @Valid TaskRequest taskRequest) {
        return new ResponseEntity<>(taskService.updateTask(taskRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response<List<Task>>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Task>> getTaskById(@PathVariable UUID id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteTaskById(@PathVariable UUID id) {
        return new ResponseEntity<>(taskService.deleteTask(id), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Response<List<Task>>> getTasksByCompletionStatus(@RequestParam Boolean completed) {
        return new ResponseEntity<>(taskService.getTasksByCompletionStatus(completed), HttpStatus.OK);
    }

    @GetMapping("/priority")
    public ResponseEntity<Response<List<Task>>> getTasksByPriority(@RequestParam String priority) {
        return new ResponseEntity<>(taskService.getTasksByPriority(priority), HttpStatus.OK);
    }
}
