package com.chat.server.controller.api;

import com.chat.server.model.request.CreateUserRequest;
import com.chat.server.model.request.LoginRequest;
import com.chat.server.model.response.CreateUserResponse;
import com.chat.server.model.response.LoginResponse;
import com.chat.server.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @PostMapping("/create-user")
    public CreateUserResponse createUser(@RequestBody @Valid CreateUserRequest request) {
        return authService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}
