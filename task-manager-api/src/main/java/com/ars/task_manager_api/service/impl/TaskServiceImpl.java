package com.ars.task_manager_api.service.impl;

import com.ars.task_manager_api.dto.request.TaskRequest;
import com.ars.task_manager_api.dto.response.Response;
import com.ars.task_manager_api.entity.Task;
import com.ars.task_manager_api.entity.User;
import com.ars.task_manager_api.enumeration.Priority;
import com.ars.task_manager_api.exception.ResourceNotFoundException;
import com.ars.task_manager_api.repository.TaskRepository;
import com.ars.task_manager_api.service.TaskService;
import com.ars.task_manager_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;


    @Override
    public Response<Task> createTask(TaskRequest taskRequest) {
        log.info("inside createTask()");
        User loggedInUser = userService.getCurrentLoggedInUser();
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .priority(taskRequest.getPriority())
                .dueDate(taskRequest.getDueDate())
                .completed(taskRequest.getCompleted())
                .user(loggedInUser)
                .build();
        Task savedTask = taskRepository.save(task);

        return Response.<Task>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Task created successfully")
                .data(savedTask)
                .build();
    }

    @Override
    @Transactional
    public Response<List<Task>> getAllTasks() {
        log.info("inside getAllTasks()");
        User loggedInUser = userService.getCurrentLoggedInUser();
        List<Task> tasks = taskRepository.findByUser(loggedInUser, Sort.by(Sort.Direction.DESC, "dueDate"));
        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tasks retrieved successfully")
                .data(tasks)
                .build();
    }

    @Override
    public Response<Task> getTaskById(UUID taskId) {
        log.info("inside getTaskById()");

        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new ResourceNotFoundException("Task with id: "+ taskId +" not found"));

        return Response.<Task> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task retrieved successfully")
                .data(task)
                .build();
    }

    @Override
    public Response<Task> updateTask(TaskRequest taskRequest) {
        log.info("inside updateTask()");
        Task task = taskRepository.findById(taskRequest.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Task with id: "+ taskRequest.getId() +" not found"));
        if(null != task.getTitle()) task.setTitle(taskRequest.getTitle());
        if(null != task.getDescription()) task.setDescription(taskRequest.getDescription());
        if(null != task.getPriority()) task.setPriority(taskRequest.getPriority());
        if(null != task.getDueDate()) task.setDueDate(taskRequest.getDueDate());
        if(null != task.getCompleted()) task.setCompleted(taskRequest.getCompleted());
        if(null != task.getUser()) task.setUser(task.getUser());

        taskRepository.save(task);
        return Response.<Task>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task updated successfully")
                .data(task)
                .build();

    }

    @Override
    public Response<Void> deleteTask(UUID taskId) {
        log.info("inside deleteTask()");

        if(!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task with id: "+ taskId +" not found");
        }
        taskRepository.deleteById(taskId);
        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task deleted successfully")
                .build();
    }

    @Override
    @Transactional
    public Response<List<Task>> getTasksByCompletionStatus(boolean completed) {
        log.info("inside getTasksByCompletionStatus()");
        User loggedInUser = userService.getCurrentLoggedInUser();
        List<Task> tasks = taskRepository.findByCompletedAndUser(completed, loggedInUser);
        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tasks filtered by completion status for user with username : "+ loggedInUser.getUsername())
                .data(tasks)
                .build();
    }

    @Override
    public Response<List<Task>> getTasksByPriority(String priority) {
        log.info("inside getTasksByPriority()");
        User loggedInUser = userService.getCurrentLoggedInUser();
        Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
        List<Task> tasks = taskRepository
                .findByPriorityAndUser(priorityEnum, loggedInUser, Sort.by(Sort.Direction.DESC, "dueDate"));
        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tasks filtered by priority for user")
                .data(tasks)
                .build();
    }
}
