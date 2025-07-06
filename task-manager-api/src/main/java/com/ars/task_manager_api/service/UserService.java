package com.ars.task_manager_api.service;

import com.ars.task_manager_api.dto.request.UserRequest;
import com.ars.task_manager_api.dto.response.Response;
import com.ars.task_manager_api.entity.User;

public interface UserService {
    Response<?> signUp(UserRequest userRequest);
    Response<?> login(UserRequest userRequest);
    User getCurrentLoggedInUser();
}
