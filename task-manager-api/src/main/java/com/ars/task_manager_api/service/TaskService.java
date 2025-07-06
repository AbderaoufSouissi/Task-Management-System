package com.ars.task_manager_api.service;

import com.ars.task_manager_api.dto.request.TaskRequest;
import com.ars.task_manager_api.dto.response.Response;
import com.ars.task_manager_api.entity.Task;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    Response<Task> createTask(TaskRequest taskRequest);
    Response<List<Task>> getAllTasks();
    Response<Task> getTaskById(UUID taskId);
    Response<Task> updateTask(TaskRequest taskRequest);
    Response<Void> deleteTask(UUID taskId);
    Response<List<Task>> getTasksByCompletionStatus(boolean completed);
    Response<List<Task>> getTasksByPriority(String priority);
}
