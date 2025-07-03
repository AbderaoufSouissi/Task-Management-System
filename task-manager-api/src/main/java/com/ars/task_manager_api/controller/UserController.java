package com.ars.task_manager_api.controller;

import com.ars.task_manager_api.dto.request.UserRequest;
import com.ars.task_manager_api.dto.response.Response;
import com.ars.task_manager_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<Response<?>> signUp(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.signUp(userRequest));
    }


    @GetMapping("/login")
    public ResponseEntity<Response<?>> login(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }
}
