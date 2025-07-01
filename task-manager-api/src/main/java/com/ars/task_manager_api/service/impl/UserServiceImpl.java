package com.ars.task_manager_api.service.impl;

import com.ars.task_manager_api.dto.request.UserRequest;
import com.ars.task_manager_api.dto.response.Response;
import com.ars.task_manager_api.entity.User;
import com.ars.task_manager_api.enumeration.Role;
import com.ars.task_manager_api.exception.BadRequestException;
import com.ars.task_manager_api.exception.ResourceNotFoundException;
import com.ars.task_manager_api.repository.UserRepository;
import com.ars.task_manager_api.security.JwtUtils;
import com.ars.task_manager_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    public Response<?> signUp(UserRequest userRequest) {
        log.info("Inside signUp method");
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Username is already in use");
        }
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User registered successfully")
                .build();

    }

    @Override
    public Response<?> login(UserRequest userRequest) {
        log.info("Inside login method");
        User user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if(!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong password");
        }
        String token = jwtUtils.generateToken(user.getUsername());
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully logged in")
                .data(token)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        log.info("Inside getCurrentLoggedInUser method");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
}
