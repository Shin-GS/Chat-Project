package com.chat.server.controller.api;

import com.chat.server.common.Response;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.model.request.CreateUserRequest;
import com.chat.server.model.request.LoginRequest;
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
    public Response<Object> createUser(@RequestBody @Valid CreateUserRequest request) {
        authService.createUser(request);
        return Response.of(SuccessCode.Success);
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return Response.of(SuccessCode.Success, authService.login(request));
    }
}
